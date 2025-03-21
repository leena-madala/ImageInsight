package com.leena.imageinsight.exception;

import com.leena.imageinsight.model.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodParameter methodParameter;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid parameter");

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getStatus());
            assertEquals("Invalid Parameter", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("Invalid parameter"));
        } catch (Exception e) {
            fail("Exception thrown during handling IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        String invalidValue = "invalid";
        Class<String> expectedType = String.class;
        String parameterName = "param";

        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                invalidValue, expectedType, parameterName, methodParameter, new Throwable("parameter type mismatch")
        );

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleMethodArgumentTypeMismatchException(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getStatus());
            assertEquals("Invalid argument type", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("Invalid value 'invalid' for parameter 'param'"));
        } catch (Exception e) {
            fail("Exception thrown during handling MethodArgumentTypeMismatchException: " + e.getMessage());
        }
    }

    @Test
    void testHandleMissingParameter() {
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("param", "String");

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleMissingParameter(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getStatus());
            assertEquals("Missing parameter", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("Parameter 'param' is required"));
        } catch (Exception e) {
            fail("Exception thrown during handling MissingServletRequestParameterException: " + e.getMessage());
        }
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violation", Collections.emptySet());

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleConstraintViolationException(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.BAD_REQUEST.value(), apiResponse.getStatus());
            assertEquals("Invalid Parameter", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().isEmpty());
        } catch (Exception e) {
            fail("Exception thrown during handling ConstraintViolationException: " + e.getMessage());
        }
    }

    @Test
    void testHandleApiException() {
        ApiException exception = new ApiException(500, "API Error");

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleApiException(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(500, apiResponse.getStatus());
            assertEquals("API Error", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("API Error"));
        } catch (Exception e) {
            fail("Exception thrown during handling ApiException: " + e.getMessage());
        }
    }

    @Test
    void testHandleGlobalExceptions() {
        Exception exception = new Exception("Unexpected error");

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleGlobalExceptions(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), apiResponse.getStatus());
            assertEquals("Internal Server Error", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("An unexpected error occurred while processing the request."));
        } catch (Exception e) {
            fail("Exception thrown during handling global exceptions: " + e.getMessage());
        }
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        try {
            ResponseEntity<ApiResponse> responseEntity = globalExceptionHandler.handleResourceNotFoundException(exception);
            ApiResponse apiResponse = responseEntity.getBody();
            assertNotNull(apiResponse);
            assertEquals(HttpStatus.NOT_FOUND.value(), apiResponse.getStatus());
            assertEquals("Resource Not Found", apiResponse.getMessage());
            assertTrue(apiResponse.getErrors().contains("Resource not found"));
        } catch (Exception e) {
            fail("Exception thrown during handling ResourceNotFoundException: " + e.getMessage());
        }
    }
}