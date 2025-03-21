package com.leena.imageinsight.model.dto;

import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testValidImageDto() {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", "Test Image", List.of("object1", "object2"));
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors.");
    }

    @Test
    void testInvalidUrl() {
        ImageDto imageDto = new ImageDto(1L, "invalid-url", "Test Image", List.of("object1", "object2"));
        assertFalse(validator.validate(imageDto).isEmpty(), "Expected validation errors for invalid URL.");
    }

    @Test
    void testNullOrEmptyUrl() {
        ImageDto imageDtoNull = new ImageDto(1L, null, "Test Image", List.of("object1", "object2"));
        ImageDto imageDtoEmpty = new ImageDto(1L, "", "Test Image", List.of("object1", "object2"));

        assertFalse(validator.validate(imageDtoNull).isEmpty(), "Expected validation error for null URL.");
        assertFalse(validator.validate(imageDtoEmpty).isEmpty(), "Expected validation error for empty URL.");
    }

    @Test
    void testValidImageDtoWithoutObjects() {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", "Test Image", null);
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors.");
    }

    @Test
    void testValidImageDtoWithoutLabel() {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", null, List.of("object1", "object2"));
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors.");
    }

    @Test
    void testExcessivelyLongLabel() {
        String longLabel = "a".repeat(1000); // Assuming no max size constraint in validation rules
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", longLabel, List.of("object1"));

        // Since no size constraint is defined, this should pass
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors for long label.");
    }

    @Test
    void testEmptyObjectsInImage() {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", "Test Image", List.of());
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors for empty object list.");
    }

    @Test
    void testObjectsInImageWithDuplicateEntries() {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image.jpg", "Test Image", List.of("object1", "object1"));

        // Assuming duplicates are allowed
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors for duplicate entries in objects.");
    }

    @Test
    void testValidUrlWithIpAddress() {
        ImageDto imageDto = new ImageDto(1L, "https://192.168.1.1/image.jpg", "Test Image", List.of("object1"));
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors for URL with IP address.");
    }

    @Test
    void testConstructorWithoutId() {
        ImageDto imageDto = new ImageDto("https://example.com/image.jpg", "Test Image", List.of("object1"));
        assertTrue(validator.validate(imageDto).isEmpty(), "Expected no validation errors for constructor without ID.");
    }
}