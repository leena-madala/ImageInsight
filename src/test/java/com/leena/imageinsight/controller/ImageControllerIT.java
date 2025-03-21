package com.leena.imageinsight.controller;

import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ImageController.class)
class ImageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    void shouldUploadImageMetadata() throws Exception {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image1.jpg", "Image 1", null);
        when(imageService.uploadImageMetadata(Mockito.any())).thenReturn(imageDto);

        mockMvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://example.com/image1.jpg\",\"label\":\"Image 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image metadata saved successfully"))
                .andExpect(jsonPath("$.data.url").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$.data.label").value("Image 1"));
    }

    @Test
    void shouldReturnImageById() throws Exception {
        ImageDto imageDto = new ImageDto(1L, "https://example.com/image1.jpg", "Image 1", List.of("cat", "animal"));
        when(imageService.getImageById(1L)).thenReturn(Optional.of(imageDto));

        mockMvc.perform(get("/images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image retrieved successfully"))
                .andExpect(jsonPath("$.data.url").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$.data.label").value("Image 1"))
                .andExpect(jsonPath("$.data.objectsInImage[0]").value("cat"))
                .andExpect(jsonPath("$.data.objectsInImage[1]").value("animal"));
    }

    @Test
    void shouldReturnAllImages() throws Exception {
        List<ImageDto> images = List.of(
                new ImageDto(1L, "https://example.com/image1.jpg", "Image 1", List.of("cat", "animal")),
                new ImageDto(2L, "https://example.com/image2.jpg", "Image 2", List.of("dog", "animal"))
        );
        when(imageService.getAllImages()).thenReturn(images);

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image(s) retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$.data[1].url").value("https://example.com/image2.jpg"));
    }

    @Test
    void shouldReturnImagesByObjects() throws Exception {
        List<ImageDto> images = List.of(
                new ImageDto(1L, "https://example.com/image1.jpg", "Image 1", List.of("cat", "animal")),
                new ImageDto(2L, "https://example.com/image2.jpg", "Image 2", List.of("cat", "animal"))
        );
        when(imageService.getImagesByObjects("cat")).thenReturn(images);

        mockMvc.perform(get("/images?objects=cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image(s) retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].url").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$.data[1].url").value("https://example.com/image2.jpg"));
    }

    @Test
    void shouldReturnBadRequestWhenObjectsParamIsEmpty() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/images?objects="))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Invalid Parameter"))
                    .andExpect(jsonPath("$.errors[0]").value("Objects parameter cannot be empty"));
        });
    }

    @Test
    void shouldReturnNoImagesFoundMessage() throws Exception {
        List<ImageDto> images = List.of();  // Empty list of images
        when(imageService.getAllImages()).thenReturn(images);

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No images uploaded yet in the system."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldReturnEmptyResultForValidObjectsButNoMatch() throws Exception {
        when(imageService.getImagesByObjects("nonexistentObject")).thenReturn(List.of());

        mockMvc.perform(get("/images?objects=nonexistentObject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("No images found containing the specified objects: nonexistentObject"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}