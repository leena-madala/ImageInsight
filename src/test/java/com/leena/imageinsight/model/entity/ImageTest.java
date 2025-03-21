package com.leena.imageinsight.model.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImageTest {

    @Test
    void testImageConstructorWithUrlLabelAndObjects() {
        String url = "https://example.com/image.jpg";
        String label = "Test Image";
        List<String> objectsInImage = List.of("object1", "object2");

        Image image = new Image(url, label, objectsInImage);

        assertEquals(url, image.getUrl());
        assertEquals(label, image.getLabel());
        assertEquals(objectsInImage, image.getObjectsInImage());
    }

    @Test
    void testImageConstructorWithIdUrlLabelAndObjects() {
        Long id = 1L;
        String url = "https://example.com/image.jpg";
        String label = "Test Image";
        List<String> objectsInImage = List.of("object1", "object2");

        Image image = new Image(id, url, label, objectsInImage);

        assertEquals(id, image.getId());
        assertEquals(url, image.getUrl());
        assertEquals(label, image.getLabel());
        assertEquals(objectsInImage, image.getObjectsInImage());
    }

    @Test
    void testSettersAndGetters() {
        Image image = new Image();

        image.setId(1L);
        image.setUrl("https://example.com/image.jpg");
        image.setLabel("Test Image");
        image.setObjectsInImage(List.of("object1", "object2"));

        assertEquals(1L, image.getId());
        assertEquals("https://example.com/image.jpg", image.getUrl());
        assertEquals("Test Image", image.getLabel());
        assertEquals(List.of("object1", "object2"), image.getObjectsInImage());
    }

    @Test
    void testEmptyObjectsInImage() {
        String url = "https://example.com/image.jpg";
        String label = "Test Image";
        List<String> objectsInImage = List.of();

        Image image = new Image(url, label, objectsInImage);

        assertTrue(image.getObjectsInImage().isEmpty(), "Objects in image should be empty.");
    }

    @Test
    void testNullObjectsInImage() {
        String url = "https://example.com/image.jpg";
        String label = "Test Image";

        Image image = new Image(url, label, null);

        assertNull(image.getObjectsInImage(), "Objects in image should be null.");
    }
}