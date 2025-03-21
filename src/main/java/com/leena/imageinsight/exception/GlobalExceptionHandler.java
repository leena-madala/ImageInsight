package com.leena.imageinsight.exception;

import com.leena.imageinsight.model.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException occurred: {}", ex.getMessage(), ex);

        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Parameter",
                Collections.singletonList(ex.getMessage())
        );

        // Return the error response with status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle MethodArgumentNotValidException for validation errors (using @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred: {}", ex.getMessage(), ex);

        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Build the error response
        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errorMessages
        );

        // Return the error response with status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle MethodArgumentTypeMismatchException for invalid parameter types
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Invalid argument type error occurred: {}", ex.getMessage(), ex);

        String errorMessage = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());

        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid argument type",
                Collections.singletonList(errorMessage)
        );

        // Return the error response with status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        log.error("Missing parameter error: {}", ex.getMessage(), ex);

        String errorMessage = String.format("Parameter '%s' is required", ex.getParameterName());

        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Missing parameter",
                Collections.singletonList(errorMessage)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // Handle ConstraintViolationException (for parameter validation with annotations like @NotNull, @Size)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error occurred: {}", ex.getMessage(), ex);

        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Parameter",
                errorMessages
        );

        // Return the error response with status 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        int statusCode = ex.getStatusCode();
        String errorMessage = ex.getMessage();

        log.error("API Exception: {}", errorMessage, ex);

        ApiResponse errorResponse = new ApiResponse(
                statusCode,
                "API Error",
                List.of(errorMessage)
        );

        // Return the error response with the status code from the ApiException
        return ResponseEntity.status(HttpStatus.valueOf(statusCode)).body(errorResponse);
    }

    // Handle general exceptions (e.g., unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalExceptions(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        // Create a generic error message for unexpected errors
        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                List.of("An unexpected error occurred while processing the request.")
        );

        // Return the error response with status 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Handle resource not found exceptions (e.g., when an entity is not found in the database)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);

        ApiResponse errorResponse = new ApiResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                List.of(ex.getMessage())
        );

        // Return the error response with status 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Additional handlers for different exceptions like Unauthorized, Forbidden, etc.

}
