package com.leena.imageinsight.service;

import com.leena.imageinsight.externalapi.ImaggaService;
import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.model.entity.Image;
import com.leena.imageinsight.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImaggaService imaggaService;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUploadImageMetadata() {
        String imageUrl = "https://example.com/image.jpg";
        String label = "Test Image";
        List<String> objectsInImage = List.of("object1", "object2");
        ImageDto imageDto = new ImageDto(null, imageUrl, label, null);
        when(imaggaService.getObjectsInImage(imageUrl)).thenReturn(objectsInImage);

        when(imageRepository.save(any(Image.class)))
                .thenAnswer(invocation -> {
                    Image imageToSave = invocation.getArgument(0, Image.class);
                    imageToSave.setId(1L); // Simulate repository assigning an ID
                    return imageToSave;
                });

        ImageDto result = imageService.uploadImageMetadata(imageDto);

        assertNotNull(result);
        assertEquals(imageUrl, result.getUrl());
        assertEquals(label, result.getLabel());
        assertEquals(objectsInImage, result.getObjectsInImage());
        verify(imaggaService, times(1)).getObjectsInImage(imageUrl);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void shouldGetAllImages() {
        List<Image> images = List.of(
                new Image("https://example1.com", "Image 1", List.of("object1")),
                new Image("https://example2.com", "Image 2", List.of("object2"))
        );
        when(imageRepository.findAll()).thenReturn(images);

        List<ImageDto> result = imageService.getAllImages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("https://example1.com", result.get(0).getUrl());
        assertEquals("Image 1", result.get(0).getLabel());
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void shouldGetImageById() {
        Long imageId = 1L;
        Image image = new Image(imageId, "https://example.com/image.jpg", "Test Image", List.of("object1"));
        when(imageRepository.findById(eq(imageId))).thenReturn(Optional.of(image));

        Optional<ImageDto> result = imageService.getImageById(imageId);

        assertTrue(result.isPresent());
        assertEquals(imageId, result.get().getId());
        assertEquals("Test Image", result.get().getLabel());
        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void shouldReturnEmptyWhenImageNotFoundById() {
        Long imageId = 999L;
        when(imageRepository.findById(eq(imageId))).thenReturn(Optional.empty());

        Optional<ImageDto> result = imageService.getImageById(imageId);

        assertFalse(result.isPresent());
        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void shouldGetImagesByObjects() {
        List<String> objectList = List.of("object1", "object2");
        List<Image> images = List.of(
                new Image("https://example1.com", "Image 1", List.of("object1")),
                new Image("https://example2.com", "Image 2", List.of("object2"))
        );
        when(imageRepository.findImagesContainingObjects(eq(objectList))).thenReturn(images);

        List<ImageDto> result = imageService.getImagesByObjects("object1,object2");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("https://example1.com", result.get(0).getUrl());
        verify(imageRepository, times(1)).findImagesContainingObjects(objectList);
    }
}