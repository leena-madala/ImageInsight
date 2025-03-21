package com.leena.imageinsight.controller;

import com.leena.imageinsight.exception.GlobalExceptionHandler;
import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Add GlobalExceptionHandler
                .build();
    }

    @Test
    void shouldUploadImageMetadata() throws Exception {
        ImageDto imageDto = new ImageDto(1L,
                                        "https://example.com/image1.jpg",
                                        "Image 1", null);
        when(imageService.uploadImageMetadata(any())).thenReturn(imageDto);

        mockMvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://example.com/image1.jpg\",\"label\":\"Image 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image metadata saved successfully"))
                .andExpect(jsonPath("$.data.url").value("https://example.com/image1.jpg"));
    }

    @Test
    public void shouldReturnBadRequestForInvalidUrl() throws Exception {
        String invalidUrlJson = "{\"url\":\"invalid-url\"}";

        mockMvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUrlJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0]").value("Invalid URL format."));
    }

    @Test
    void shouldReturnBadRequestForNullUrlInPost() throws Exception {
        String payloadWithNullUrl = "{\"url\":null,\"label\":\"Image 1\"}";

        mockMvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadWithNullUrl))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0]").value("Image URL is required."));
    }

    @Test
    void shouldReturnBadRequestForMissingUrlInPost() throws Exception {
        String payloadWithoutUrl = "{\"label\":\"Image 1\"}";

        mockMvc.perform(post("/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payloadWithoutUrl))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0]").value("Image URL is required."));
    }

    @Test
    void shouldGetImageById() throws Exception {
        ImageDto imageDto = new ImageDto(1L,
                                        "https://example.com/image1.jpg",
                                        "Image 1", null);
        when(imageService.getImageById(1L)).thenReturn(java.util.Optional.of(imageDto));

        mockMvc.perform(get("/images/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image retrieved successfully"))
                .andExpect(jsonPath("$.data.url").value("https://example.com/image1.jpg"));
    }

    @Test
    void shouldReturnNotFoundForNonExistentImage() throws Exception {
        when(imageService.getImageById(999L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/images/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource Not Found"))
                .andExpect(jsonPath("$.errors[0]").value("Image not found with id: 999"));
    }

    @Test
    void shouldReturnBadRequestForInvalidImageIdFormat() throws Exception {
        mockMvc.perform(get("/images/{imageId}", "abc"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid argument type"))
                .andExpect(jsonPath("$.errors[0]")
                        .value("Invalid value 'abc' for parameter 'imageId'"))
                .andReturn();
    }

    @Test
    void shouldGetAllImages() throws Exception {
        ImageDto image1 = new ImageDto(1L, "https://example.com/image1.jpg", "Image 1", null);
        ImageDto image2 = new ImageDto(2L, "https://example.com/image2.jpg", "Image 2", null);
        when(imageService.getAllImages()).thenReturn(java.util.List.of(image1, image2));

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image(s) retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void shouldReturnNoImagesWhenDatabaseIsEmpty() throws Exception {
        when(imageService.getAllImages()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No images uploaded yet in the system."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldGetImagesFilteredByObject() throws Exception {
        ImageDto image1 = new ImageDto(1L,
                                        "https://example.com/image1.jpg",
                                        "Image 1", java.util.List.of("object1"));
        when(imageService.getImagesByObjects("object1")).thenReturn(java.util.List.of(image1));

        mockMvc.perform(get("/images?objects=object1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Image(s) retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void shouldReturnEmptyResultForValidObjectsButNoMatch() throws Exception {
        when(imageService.getImagesByObjects("nonexistentObject")).thenReturn(java.util.List.of());

        mockMvc.perform(get("/images?objects=nonexistentObject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("No images found containing the specified objects: nonexistentObject"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldReturnBadRequestForEmptyObjectsParam() throws Exception {
        mockMvc.perform(get("/images?objects="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Parameter"))
                .andExpect(jsonPath("$.errors[0]").value("Objects parameter cannot be empty"));
    }
}