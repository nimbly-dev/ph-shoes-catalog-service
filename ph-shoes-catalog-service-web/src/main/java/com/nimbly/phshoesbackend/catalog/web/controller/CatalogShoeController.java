package com.nimbly.phshoesbackend.catalog.web.controller;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.CatalogShoeSpecifications;
import com.nimbly.phshoesbackend.catalog.core.service.CatalogShoeService;
import com.nimbly.phshoesbackend.catalog.web.api.CatalogProductsApi;
import com.nimbly.phshoesbackend.catalog.web.api.model.CatalogShoePage;
import com.nimbly.phshoesbackend.catalog.web.api.model.LatestBrandData;
import com.nimbly.phshoesbackend.catalog.web.mappers.CatalogShoeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CatalogShoeController implements CatalogProductsApi {
    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "ageGroup",
            "brand",
            "day",
            "dwid",
            "gender",
            "id",
            "month",
            "priceOriginal",
            "priceSale",
            "subtitle",
            "title",
            "year"
    );
    private static final Map<String, String> SORT_FIELD_MAPPINGS = Map.ofEntries(
            Map.entry("ageGroup", "ageGroup"),
            Map.entry("brand", "brand"),
            Map.entry("day", "day"),
            Map.entry("dwid", "dwid"),
            Map.entry("gender", "gender"),
            Map.entry("id", "id"),
            Map.entry("month", "month"),
            Map.entry("priceOriginal", "priceOriginal"),
            Map.entry("priceSale", "priceSale"),
            Map.entry("subtitle", "subtitle"),
            Map.entry("title", "title"),
            Map.entry("year", "year")
    );

    private final CatalogShoeService service;
    private final CatalogShoeMapper mapper;

    public CatalogShoeController(CatalogShoeService service, CatalogShoeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<CatalogShoePage> listCatalogShoes(
            String brand,
            String gender,
            String ageGroup,
            LocalDate date,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            List<String> sizes,
            Boolean onSale,
            Double minPrice,
            Double maxPrice,
            Integer page,
            Integer size,
            String sort
    ) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be on or before endDate.");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be less than or equal to maxPrice.");
        }
        Specification<CatalogShoe> specification =
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (StringUtils.hasText(brand)) {
            specification = specification.and(CatalogShoeSpecifications.hasBrand(brand));
        }
        if (StringUtils.hasText(gender)) {
            specification = specification.and(CatalogShoeSpecifications.hasGender(gender));
        }
        if (StringUtils.hasText(ageGroup)) {
            specification = specification.and(CatalogShoeSpecifications.hasAgeGroup(ageGroup));
        }

        if (date != null) {
            specification = specification.and(CatalogShoeSpecifications.collectedOn(date));
        } else if (startDate != null && endDate != null) {
            specification = specification.and(CatalogShoeSpecifications.collectedBetween(startDate, endDate));
        }

        List<String> normalizedSizes = normalizeSizes(sizes);
        if (!normalizedSizes.isEmpty()) {
            specification = specification.and(CatalogShoeSpecifications.hasAnySize(normalizedSizes));
        }

        if (StringUtils.hasText(keyword)) {
            specification = specification.and(CatalogShoeSpecifications.hasKeyword(keyword));
        }

        if (Boolean.TRUE.equals(onSale)) {
            specification = specification.and(CatalogShoeSpecifications.isOnSale());
        }

        if (minPrice != null && maxPrice != null) {
            specification = specification.and(CatalogShoeSpecifications.finalPriceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            specification = specification.and(CatalogShoeSpecifications.finalPriceGte(minPrice));
        } else if (maxPrice != null) {
            specification = specification.and(CatalogShoeSpecifications.finalPriceLte(maxPrice));
        }

        Pageable pageable = buildPageable(page, size, sort);
        Page<CatalogShoe> results = service.fetchBySpec(specification, pageable);
        return ResponseEntity.ok(mapper.toPage(results));
    }

    @Override
    public ResponseEntity<List<LatestBrandData>> getLatestCatalogShoes() {
        List<LatestBrandData> payload = service.getLatestDataByBrand()
                .stream()
                .map(mapper::toLatestBrandData)
                .toList();
        return ResponseEntity.ok(payload);
    }

    private Pageable buildPageable(Integer page, Integer size, String sort) {
        int safePage = Math.max(0, Optional.ofNullable(page).orElse(0));
        int safeSize = Optional.ofNullable(size).orElse(20);
        safeSize = Math.max(1, Math.min(2500, safeSize));

        Sort sortSpec = Sort.unsorted();
        if (StringUtils.hasText(sort)) {
            String[] parts = sort.split(",");
            if (parts.length > 2) {
                throw new IllegalArgumentException("Only a single sort field is supported.");
            }

            String sortField = parts[0].trim();
            if (!sortField.isEmpty()) {
                String mappedField = SORT_FIELD_MAPPINGS.get(sortField);
                if (mappedField == null) {
                    throw new IllegalArgumentException("Unsupported sort field: " + sortField
                            + ". Allowed fields: " + String.join(", ", ALLOWED_SORT_FIELDS));
                }

                Sort.Direction direction = Sort.Direction.ASC;
                if (parts.length > 1) {
                    String rawDirection = parts[1].trim();
                    if ("desc".equalsIgnoreCase(rawDirection)) {
                        direction = Sort.Direction.DESC;
                    } else if (!rawDirection.isEmpty() && !"asc".equalsIgnoreCase(rawDirection)) {
                        throw new IllegalArgumentException("Unsupported sort direction: " + rawDirection
                                + ". Use asc or desc.");
                    }
                }
                sortSpec = Sort.by(direction, mappedField);
            }
        }
        return PageRequest.of(safePage, safeSize, sortSpec);
    }

    private List<String> normalizeSizes(List<String> sizes) {
        if (sizes == null || sizes.isEmpty()) {
            return List.of();
        }
        return sizes.stream()
                .flatMap(raw -> Arrays.stream(raw.split(",")))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
    }

}

