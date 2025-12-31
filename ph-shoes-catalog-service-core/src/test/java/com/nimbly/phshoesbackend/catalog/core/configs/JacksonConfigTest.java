package com.nimbly.phshoesbackend.catalog.core.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonConfigTest {
    @Test
    void strictObjectMapper_disablesLenientFeatures() {
        // Arrange
        JacksonConfig config = new JacksonConfig();

        // Act
        ObjectMapper mapper = config.strictObjectMapper();

        // Assert
        assertThat(mapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
        assertThat(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isTrue();
        assertThat(mapper.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).isTrue();
        assertThat(mapper.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)).isTrue();
    }
}
