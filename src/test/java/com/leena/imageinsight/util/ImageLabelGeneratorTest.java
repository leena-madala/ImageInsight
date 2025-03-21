package com.leena.imageinsight.util;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ImageLabelGeneratorTest {

    @Test
    public void testGenerateLabelWithImageUrlAndImageObjects() {
        List<String> imageObjects = List.of("cat", "dog");
        String imageUrl = "https://example.com/images/cat_123.jpg";

        String result = ImageLabelGenerator.generateLabel(imageUrl, imageObjects);

        assertEquals("example_cat", result, "Label should be generated from domain and first object.");
    }

    @Test
    public void testGenerateLabelWithImageUrlAndNoImageObjects() {
        List<String> imageObjects = List.of();
        String imageUrl = "https://example.com/images/cat_123.jpg";

        String result = ImageLabelGenerator.generateLabel(imageUrl, imageObjects);

        assertEquals("example_cat", result, "Label should be generated from domain and image name when no objects are provided.");
    }

    @Test
    public void testExtractDomain() {
        String imageUrl = "https://www.example.com/images/cat.jpg";
        String result = ImageLabelGenerator.extractDomain(imageUrl);

        assertEquals("example", result, "Domain should be extracted correctly, without 'www.' and TLD.");
    }

    @Test
    public void testFormatLabel() {
        String text = "Test Image_123";
        String result = ImageLabelGenerator.formatLabel(text);

        assertEquals("test_image", result, "Label should be formatted to remove digits and replace spaces with underscores.");
    }

    @Test
    public void testFormatLabelWithEmptyText() {
        String result = ImageLabelGenerator.formatLabel("");

        assertEquals("", result, "Empty text should return an empty string.");
    }

    @Test
    public void testFormatLabelWithNullText() {
        String result = ImageLabelGenerator.formatLabel(null);

        assertEquals("", result, "Null text should return an empty string.");
    }

    @Test
    public void testFormatLabelWithOnlyDigits() {
        String result = ImageLabelGenerator.formatLabel("123456");

        assertEquals("", result, "Text with only digits should return an empty string.");
    }
}