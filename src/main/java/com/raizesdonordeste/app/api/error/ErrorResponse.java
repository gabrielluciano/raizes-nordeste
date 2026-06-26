package com.raizesdonordeste.app.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String error,
        String message,
        List<FieldError> details,
        Instant timestamp,
        String path
) {

    public record FieldError(String field, String issue) {
    }

    public static ErrorResponse of(String error, String message, String path) {
        return new ErrorResponse(error, message, null, Instant.now(), path);
    }

    public static ErrorResponse of(String error, String message, List<FieldError> details, String path) {
        return new ErrorResponse(error, message, details, Instant.now(), path);
    }
}
