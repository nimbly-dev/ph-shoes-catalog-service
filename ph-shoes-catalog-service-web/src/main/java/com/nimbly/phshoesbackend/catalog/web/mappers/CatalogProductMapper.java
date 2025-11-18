package com.nimbly.phshoesbackend.catalog.web.mappers;

import com.nimbly.phshoesbackend.catalog.core.model.FactProductShoes;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.FactProductShoesSpecRepository;
import com.nimbly.phshoesbackend.catalog.web.api.model.FactProductShoePage;
import com.nimbly.phshoesbackend.catalog.web.api.model.FactProductShoePageContentInner;
import com.nimbly.phshoesbackend.catalog.web.api.model.LatestBrandData;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CatalogProductMapper {

    public FactProductShoePage toPage(Page<FactProductShoes> results) {
        return new FactProductShoePage()
                .content(results.getContent().stream().map(this::toContentDto).collect(Collectors.toList()))
                .page(results.getNumber())
                .size(results.getSize())
                .totalElements(results.getTotalElements())
                .totalPages(results.getTotalPages())
                .first(results.isFirst())
                .last(results.isLast())
                .empty(results.isEmpty());
    }

    private FactProductShoePageContentInner toContentDto(FactProductShoes entity) {
        FactProductShoePageContentInner dto = new FactProductShoePageContentInner();
        Optional.ofNullable(entity.getKey()).ifPresent(key -> {
            dto.setId(key.getId());
            dto.setDwid(key.getDwid());
        });
        dto.setBrand(entity.getBrand());
        dto.setTitle(entity.getTitle());
        dto.setSubtitle(entity.getSubtitle());
        dto.setUrl(toUri(entity.getUrl()));
        dto.setImage(toUri(entity.getImage()));
        dto.setPriceSale(entity.getPriceSale());
        dto.setPriceOriginal(entity.getPriceOriginal());
        dto.setGender(entity.getGender());
        dto.setAgeGroup(entity.getAgeGroup());
        dto.setYear(entity.getYear());
        dto.setMonth(entity.getMonth());
        dto.setDay(entity.getDay());
        dto.setExtra(entity.getExtra());
        return dto;
    }

    public LatestBrandData toLatestBrandData(FactProductShoesSpecRepository.LatestData latestData) {
        return new LatestBrandData()
                .brand(latestData.getBrand())
                .latestDwid(latestData.getLatestDwid());
    }

    private URI toUri(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return URI.create(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
