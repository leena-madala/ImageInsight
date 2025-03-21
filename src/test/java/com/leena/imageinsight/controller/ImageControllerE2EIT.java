package com.leena.imageinsight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leena.imageinsight.model.dto.ApiResponse;
import com.leena.imageinsight.model.dto.ImageDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// TODO: Consider using Cucumber for end-to-end tests.
// It offers better workflow support and improved readability.

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ImageControllerE2EIT {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/image-insight/images";
    }

    @Test
    void testImageControllerEndToEnd() throws Exception {
        // 1. POST /images to create an image
        ImageDto imageDto = new ImageDto(null, "https://imagga.com/static/images/tagging/wind-farm-538576_640.jpg", "Sample image", null);
        Long imageId = createImage(imageDto);

        // 2. GET /images/{id} to retrieve the created image
        ImageDto retrievedImage = retrieveImage(imageId);
        assertImageEquals(imageDto, retrievedImage);

        // 3. GET /images to retrieve all images
        List<ImageDto> allImages = retrieveAllImages();
        assertFalse(allImages.isEmpty(), "Expected at least one image in the list.");

        // 4. GET /images?objects=turbine,spring to retrieve images by object list
        List<ImageDto> imagesByObjectList = retrieveImagesByObjects();
        assertFalse(imagesByObjectList.isEmpty(), "Expected at least one image matching the object list.");
        assertTrue(imagesByObjectList.get(0).getObjectsInImage().contains("turbine"));
        assertTrue(imagesByObjectList.get(0).getObjectsInImage().contains("spring"));
    }

    private Long createImage(ImageDto imageDto) throws Exception {
        ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, imageDto, String.class);
        ApiResponse postApiResponse = parseApiResponse(postResponse);
        assertEquals("Image metadata saved successfully", postApiResponse.getMessage());

        ImageDto savedImageDto = objectMapper.convertValue(postApiResponse.getData(), ImageDto.class);
        return savedImageDto.getId();
    }

    private ImageDto retrieveImage(Long imageId) throws Exception {
        ResponseEntity<String> getByIdResponse = restTemplate.getForEntity(baseUrl + "/" + imageId, String.class);
        ApiResponse getByIdApiResponse = parseApiResponse(getByIdResponse);
        assertEquals("Image retrieved successfully", getByIdApiResponse.getMessage());

        return objectMapper.convertValue(getByIdApiResponse.getData(), ImageDto.class);
    }

    private List<ImageDto> retrieveAllImages() throws Exception {
        ResponseEntity<String> getAllResponse = restTemplate.getForEntity(baseUrl, String.class);
        ApiResponse getAllApiResponse = parseApiResponse(getAllResponse);
        assertEquals("Image(s) retrieved successfully", getAllApiResponse.getMessage());

        return objectMapper.convertValue(getAllApiResponse.getData(), new TypeReference<>() {
        });
    }

    private List<ImageDto> retrieveImagesByObjects() throws Exception {
        String objectListUrl = baseUrl + "?objects=" + "turbine,spring";
        ResponseEntity<String> getByObjectListResponse = restTemplate.getForEntity(objectListUrl, String.class);
        ApiResponse getByObjectListApiResponse = parseApiResponse(getByObjectListResponse);
        assertEquals("Image(s) retrieved successfully", getByObjectListApiResponse.getMessage());

        return objectMapper.convertValue(getByObjectListApiResponse.getData(), new TypeReference<>() {
        });
    }

    private ApiResponse parseApiResponse(ResponseEntity<String> response) throws Exception {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return objectMapper.readValue(response.getBody(), ApiResponse.class);
    }

    private void assertImageEquals(ImageDto expected, ImageDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getLabel(), actual.getLabel());
        assertNotNull(actual.getObjectsInImage(), "Objects in image should not be null.");
        assertTrue(actual.getObjectsInImage().contains("turbine"));
        assertTrue(actual.getObjectsInImage().contains("spring"));
    }
}