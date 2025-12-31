package com.nimbly.phshoesbackend.catalog.core.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogShoeIdTest {
    @Test
    void equalsAndHashCodeUseIdAndDwid() {
        // Arrange
        CatalogShoeId firstId = new CatalogShoeId();
        firstId.setId("shoe-1");
        firstId.setDwid("20240101");

        CatalogShoeId matchingId = new CatalogShoeId();
        matchingId.setId("shoe-1");
        matchingId.setDwid("20240101");

        CatalogShoeId differentId = new CatalogShoeId();
        differentId.setId("shoe-2");
        differentId.setDwid("20240102");

        // Act
        boolean matches = firstId.equals(matchingId);
        boolean differs = firstId.equals(differentId);

        // Assert
        assertThat(matches).isTrue();
        assertThat(differs).isFalse();
        assertThat(firstId.hashCode()).isEqualTo(matchingId.hashCode());
        assertThat(firstId.equals(null)).isFalse();
    }
}

