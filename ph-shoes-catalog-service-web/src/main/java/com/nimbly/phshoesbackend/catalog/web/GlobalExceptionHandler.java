package com.nimbly.phshoesbackend.catalog.web;

import com.nimbly.phshoesbackend.catalog.web.api.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String INVALID_FILTERS_MESSAGE = "Invalid catalog filters.";

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidFilters(IllegalArgumentException exception) {
        return new ErrorResponse()
                .message(INVALID_FILTERS_MESSAGE)
                .details(exception.getMessage());
    }
}
