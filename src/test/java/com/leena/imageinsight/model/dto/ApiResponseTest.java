package com.leena.imageinsight.model.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void testSuccessResponseWithData() {
        String message = "Success";
        int status = 200;
        Object data = new Object();

        ApiResponse response = new ApiResponse(status, message, data);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getData());
        assertNotNull(response.getTimestamp());
        assertNull(response.getErrors());
    }

    @Test
    void testErrorResponse() {
        String message = "Error";
        int status = 400;
        List<String> errors = List.of("Invalid input", "Missing field");

        ApiResponse response = new ApiResponse(status, message, errors);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
        assertEquals(errors, response.getErrors());
    }

    @Test
    void testConstructorWithSuccessData() {
        String message = "Data processed successfully";
        int status = 200;
        Object data = "some data";

        ApiResponse response = new ApiResponse(status, message, data);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void testConstructorWithErrorData() {
        String message = "Data processing failed";
        int status = 500;
        List<String> errors = List.of("Error 1", "Error 2");

        ApiResponse response = new ApiResponse(status, message, errors);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(errors, response.getErrors());
    }
}