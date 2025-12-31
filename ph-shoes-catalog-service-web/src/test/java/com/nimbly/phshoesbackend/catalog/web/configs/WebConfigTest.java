package com.nimbly.phshoesbackend.catalog.web.configs;

import com.nimbly.phshoesbackend.catalog.web.configs.props.CorsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class WebConfigTest {
    @Test
    void corsConfigurer_registersMappingsWithExposedHeaders() {
        // Arrange
        CorsProperties corsProperties = new CorsProperties();
        corsProperties.setAllowedOrigins(List.of("https://example.com"));
        corsProperties.setAllowedMethods(List.of("GET"));
        corsProperties.setAllowedHeaders(List.of("X-Test"));
        corsProperties.setExposedHeaders(List.of("X-Expose"));
        corsProperties.setAllowCredentials(false);
        corsProperties.setMaxAge(1200);
        WebConfig webConfig = new WebConfig(corsProperties);
        WebMvcConfigurer configurer = webConfig.corsConfigurer();

        // Act
        CorsRegistry registry = new CorsRegistry();

        // Assert
        assertThat(configurer).isNotNull();
        assertThatCode(() -> configurer.addCorsMappings(registry))
                .doesNotThrowAnyException();
    }
}
