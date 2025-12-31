package com.nimbly.phshoesbackend.catalog.web.mappers;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.web.api.model.CatalogShoePage;
import com.nimbly.phshoesbackend.catalog.web.api.model.CatalogShoePageContentInner;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogShoeMapperTest {
    @Test
    void toPage_mapsCatalogShoeToApiModel() {
        // Arrange
        CatalogShoe entity = new CatalogShoe();
        entity.setId("shoe-1");
        entity.setDwid("20240201");
        entity.setBrand("Nike");
        entity.setTitle("Zoom Runner");
        entity.setSubtitle("Road shoe");
        entity.setUrl("https://example.com/shoe");
        entity.setImage("https://example.com/image");
        entity.setPriceSale(120.0);
        entity.setPriceOriginal(150.0);
        entity.setGender("Men");
        entity.setAgeGroup("Adult");
        entity.setYear(2024);
        entity.setMonth(2);
        entity.setDay(1);
        entity.setExtra("{\"sizes\":[\"9\"]}");

        Page<CatalogShoe> results = new PageImpl<>(List.of(entity), PageRequest.of(0, 1), 1);
        CatalogShoeMapper mapper = new CatalogShoeMapper();

        // Act
        CatalogShoePage page = mapper.toPage(results);

        // Assert
        assertThat(page.getContent()).hasSize(1);
        CatalogShoePageContentInner content = page.getContent().get(0);
        assertThat(content.getId()).isEqualTo("shoe-1");
        assertThat(content.getDwid()).isEqualTo("20240201");
        assertThat(content.getBrand()).isEqualTo("Nike");
        assertThat(content.getTitle()).isEqualTo("Zoom Runner");
        assertThat(content.getSubtitle()).isEqualTo("Road shoe");
        assertThat(content.getUrl()).hasToString("https://example.com/shoe");
        assertThat(content.getImage()).hasToString("https://example.com/image");
        assertThat(content.getPriceSale()).isEqualTo(120.0);
        assertThat(content.getPriceOriginal()).isEqualTo(150.0);
        assertThat(content.getGender()).isEqualTo("Men");
        assertThat(content.getAgeGroup()).isEqualTo("Adult");
        assertThat(content.getYear()).isEqualTo(2024);
        assertThat(content.getMonth()).isEqualTo(2);
        assertThat(content.getDay()).isEqualTo(1);
        assertThat(content.getExtra()).isEqualTo("{\"sizes\":[\"9\"]}");

        assertThat(page.getPage()).isEqualTo(0);
        assertThat(page.getSize()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getFirst()).isTrue();
        assertThat(page.getLast()).isTrue();
        assertThat(page.getEmpty()).isFalse();
    }
}
