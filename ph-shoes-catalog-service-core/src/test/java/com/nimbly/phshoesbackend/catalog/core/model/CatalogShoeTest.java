package com.nimbly.phshoesbackend.catalog.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogShoeTest {
    @Test
    void settersPopulateFields() {
        // Arrange
        CatalogShoe entity = new CatalogShoe();

        // Act
        entity.setId("shoe-123");
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

        // Assert
        assertThat(entity.getId()).isEqualTo("shoe-123");
        assertThat(entity.getDwid()).isEqualTo("20240201");
        assertThat(entity.getBrand()).isEqualTo("Nike");
        assertThat(entity.getTitle()).isEqualTo("Zoom Runner");
        assertThat(entity.getSubtitle()).isEqualTo("Road shoe");
        assertThat(entity.getUrl()).isEqualTo("https://example.com/shoe");
        assertThat(entity.getImage()).isEqualTo("https://example.com/image");
        assertThat(entity.getPriceSale()).isEqualTo(120.0);
        assertThat(entity.getPriceOriginal()).isEqualTo(150.0);
        assertThat(entity.getGender()).isEqualTo("Men");
        assertThat(entity.getAgeGroup()).isEqualTo("Adult");
        assertThat(entity.getYear()).isEqualTo(2024);
        assertThat(entity.getMonth()).isEqualTo(2);
        assertThat(entity.getDay()).isEqualTo(1);
        assertThat(entity.getExtra()).isEqualTo("{\"sizes\":[\"9\"]}");
    }
}

