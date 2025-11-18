package com.nimbly.phshoesbackend.catalog.web.controller;

import com.nimbly.phshoesbackend.catalog.core.model.FactProductShoes;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.ProductShoesSpecifications;
import com.nimbly.phshoesbackend.catalog.core.service.FactProductShoesService;
import com.nimbly.phshoesbackend.catalog.web.api.CatalogProductsApi;
import com.nimbly.phshoesbackend.catalog.web.api.model.FactProductShoePage;
import com.nimbly.phshoesbackend.catalog.web.api.model.LatestBrandData;
import com.nimbly.phshoesbackend.catalog.web.mappers.CatalogProductMapper;
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
import java.util.Optional;

@RestController
public class FactProductShoesController implements CatalogProductsApi {

    private final FactProductShoesService service;
    private final CatalogProductMapper mapper;

    public FactProductShoesController(FactProductShoesService service, CatalogProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<FactProductShoePage> listFactProductShoes(
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
        Specification<FactProductShoes> spec = (root, query, cb) -> cb.conjunction();

        if (StringUtils.hasText(brand)) {
            spec = spec.and(ProductShoesSpecifications.hasBrand(brand));
        }
        if (StringUtils.hasText(gender)) {
            spec = spec.and(ProductShoesSpecifications.hasGender(gender));
        }
        if (StringUtils.hasText(ageGroup)) {
            spec = spec.and(ProductShoesSpecifications.hasAgeGroup(ageGroup));
        }

        if (date != null) {
            spec = spec.and(ProductShoesSpecifications.collectedOn(date));
        } else if (startDate != null && endDate != null) {
            spec = spec.and(ProductShoesSpecifications.collectedBetween(startDate, endDate));
        }

        List<String> normalizedSizes = normalizeSizes(sizes);
        if (!normalizedSizes.isEmpty()) {
            spec = spec.and(ProductShoesSpecifications.hasAnySize(normalizedSizes));
        }

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(ProductShoesSpecifications.hasKeyword(keyword));
        }

        if (Boolean.TRUE.equals(onSale)) {
            spec = spec.and(ProductShoesSpecifications.isOnSale());
        }

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(ProductShoesSpecifications.finalPriceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(ProductShoesSpecifications.finalPriceGte(minPrice));
        } else if (maxPrice != null) {
            spec = spec.and(ProductShoesSpecifications.finalPriceLte(maxPrice));
        }

        Pageable pageable = buildPageable(page, size, sort);
        Page<FactProductShoes> results = service.fetchBySpec(spec, pageable);
        return ResponseEntity.ok(mapper.toPage(results));
    }

    @Override
    public ResponseEntity<List<LatestBrandData>> getLatestFactProductShoes() {
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
            String property = parts[0].trim();
            if (!property.isEmpty()) {
                Sort.Direction direction = Sort.Direction.ASC;
                if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
                    direction = Sort.Direction.DESC;
                }
                sortSpec = Sort.by(direction, property);
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
