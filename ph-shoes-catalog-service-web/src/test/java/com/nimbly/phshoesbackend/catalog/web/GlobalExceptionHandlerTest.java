package com.nimbly.phshoesbackend.catalog.web;

import com.nimbly.phshoesbackend.catalog.web.api.model.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    @Test
    void handleInvalidFilters_buildsErrorResponse() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        IllegalArgumentException exception = new IllegalArgumentException("startDate must be on or before endDate.");

        // Act
        ErrorResponse response = handler.handleInvalidFilters(exception);

        // Assert
        assertThat(response.getMessage()).isEqualTo("Invalid catalog filters.");
        assertThat(response.getDetails()).isEqualTo("startDate must be on or before endDate.");
    }
}
