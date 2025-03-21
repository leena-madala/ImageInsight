package com.leena.imageinsight.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiExceptionTest {

    @Test
    void testApiExceptionConstructor() {
        int statusCode = 404;
        String errorMessage = "Resource not found";
        ApiException apiException = new ApiException(statusCode, errorMessage);

        assertNotNull(apiException);
        assertEquals(statusCode, apiException.getStatusCode());
        assertEquals(errorMessage, apiException.getMessage());
    }

    @Test
    void testApiExceptionMessage() {
        int statusCode = 500;
        String errorMessage = "Internal server error";
        ApiException apiException = new ApiException(statusCode, errorMessage);

        assertEquals("Internal server error", apiException.getMessage());
    }

    @Test
    void testApiExceptionStatusCode() {
        int statusCode = 400;
        String errorMessage = "Bad request";
        ApiException apiException = new ApiException(statusCode, errorMessage);

        assertEquals(400, apiException.getStatusCode());
    }

    @Test
    void testApiExceptionWithDifferentStatusCode() {
        int statusCode = 401;
        String errorMessage = "Unauthorized access";
        ApiException apiException = new ApiException(statusCode, errorMessage);

        assertNotNull(apiException);
        assertEquals(401, apiException.getStatusCode());
        assertEquals("Unauthorized access", apiException.getMessage());
    }
}