package com.leena.imageinsight.controller;

import com.leena.imageinsight.exception.ResourceNotFoundException;
import com.leena.imageinsight.model.dto.ApiResponse;
import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.service.ImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/images")
@Validated
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // POST /images
    @PostMapping
    public ResponseEntity<ApiResponse> uploadImageMetadata(@Valid @RequestBody ImageDto imageDto) {
        ImageDto responseDto = imageService.uploadImageMetadata(imageDto);

        ApiResponse successResponse = new ApiResponse(
                HttpStatus.OK.value(),
                "Image metadata saved successfully",
                responseDto // Return the uploaded image details as data
        );

        return ResponseEntity.ok(successResponse);
    }

    // GET /images/{imageId}
    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponse> getImageById(@PathVariable Long imageId) {
        ImageDto imageDto = imageService.getImageById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        ApiResponse successResponse = new ApiResponse(
                HttpStatus.OK.value(),
                "Image retrieved successfully",
                imageDto // Return the image data
        );

        return ResponseEntity.ok(successResponse);
    }

    /*
     * Works for both GET /images and GET /images?objects=
     *
     * Spring Boot 6.x treats trailing slashes differently.
     * To handle both '/images' and '/images/', using @GetMapping({"", "/"}) to support both cases.
     * Tried configuring `PathPatternParser` in WebMvcConfigurer and `ant_path_matcher` in properties, but neither worked.
     * Explicitly setting both paths as a workaround.
     */
    @GetMapping({"", "/"})
    public ResponseEntity<ApiResponse> getImages(@RequestParam(required = false) String objects) {
        // Check if objects parameter is provided and it's empty
        if (objects != null && objects.trim().isEmpty()) {
            throw new IllegalArgumentException("Objects parameter cannot be empty");
        }

        List<ImageDto> images;
        if (objects == null) {
            // No objects parameter provided, return all images
            images = imageService.getAllImages();
        } else {
            // Objects parameter provided, filter images by detected objects
            images = imageService.getImagesByObjects(objects);
        }

        String message = "Image(s) retrieved successfully";
        if (images.isEmpty()) {
            message = (objects == null)
                    ? "No images uploaded yet in the system."
                    : "No images found containing the specified objects: " + objects;
        }

        ApiResponse successResponse = new ApiResponse(
                HttpStatus.OK.value(),
                message,
                images // Return the list of images as data
        );
        return ResponseEntity.ok(successResponse);
    }
}