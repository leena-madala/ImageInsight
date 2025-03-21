package com.leena.imageinsight.service;

import com.leena.imageinsight.externalapi.ImaggaService;
import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.model.entity.Image;
import com.leena.imageinsight.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class ImageServiceIT {

    @Autowired
    private ImageRepository imageRepository;

    @MockBean
    private ImaggaService imaggaService;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        // Initialize the ImageService with the actual repository and mocked ImaggaService
        imageService = new ImageService(imageRepository, imaggaService);
    }

    @Test
    void shouldUploadImageMetadataAndPersistToDatabase() {
        String imageUrl = "https://example.com/image.jpg";
        String label = "Label";
        List<String> objectsInImage = List.of("object1", "object2");
        ImageDto imageDto = new ImageDto(null, imageUrl, label, null);

        when(imaggaService.getObjectsInImage(imageUrl)).thenReturn(objectsInImage);

        ImageDto result = imageService.uploadImageMetadata(imageDto);

        // Verify the result
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(imageUrl, result.getUrl());
        assertEquals(label, result.getLabel());
        assertEquals(objectsInImage, result.getObjectsInImage());

        // Verify the data is persisted in the repository
        Optional<Image> savedImage = imageRepository.findById(result.getId());
        assertTrue(savedImage.isPresent());
        assertEquals(imageUrl, savedImage.get().getUrl());
        assertEquals(label, savedImage.get().getLabel());
        assertEquals(objectsInImage, savedImage.get().getObjectsInImage());

        verify(imaggaService, times(1)).getObjectsInImage(imageUrl);
    }

    @Test
    void shouldRetrieveAllImagesFromDatabase() {
        Image image1 = new Image("https://example1.com", "Image 1", List.of("object1"));
        Image image2 = new Image("https://example2.com", "Image 2", List.of("object2"));
        imageRepository.saveAll(List.of(image1, image2));

        List<ImageDto> result = imageService.getAllImages();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("https://example1.com", result.get(0).getUrl());
        assertEquals("Image 1", result.get(0).getLabel());
    }

    @Test
    void shouldRetrieveImageById() {
        Image image = new Image("https://example.com/image.jpg", "Test Image", List.of("object1"));
        Image savedImage = imageRepository.save(image);

        Optional<ImageDto> result = imageService.getImageById(savedImage.getId());

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(savedImage.getId(), result.get().getId());
        assertEquals("Test Image", result.get().getLabel());
        assertEquals(List.of("object1"), result.get().getObjectsInImage());
    }

    @Test
    void shouldReturnEmptyWhenImageNotFoundById() {
        Optional<ImageDto> result = imageService.getImageById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldRetrieveImagesByObjects() {
        Image image1 = new Image("https://example1.com", "Image 1", List.of("object1", "object2"));
        Image image2 = new Image("https://example2.com", "Image 2", List.of("object2", "object3"));
        imageRepository.saveAll(List.of(image1, image2));

        List<ImageDto> result = imageService.getImagesByObjects("object2,object3");

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("https://example1.com", result.get(0).getUrl());
        assertEquals("https://example2.com", result.get(1).getUrl());
    }
}