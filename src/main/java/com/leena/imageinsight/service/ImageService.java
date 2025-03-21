package com.leena.imageinsight.service;

import com.leena.imageinsight.externalapi.ImaggaService;
import com.leena.imageinsight.model.dto.ImageDto;
import com.leena.imageinsight.model.entity.Image;
import com.leena.imageinsight.repository.ImageRepository;
import com.leena.imageinsight.util.ImageLabelGenerator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImaggaService imaggaService;

    public ImageService(ImageRepository imageRepository, ImaggaService imaggaService) {
        this.imageRepository = imageRepository;
        this.imaggaService = imaggaService;
    }

    // Upload an image with optional object detection
    public ImageDto uploadImageMetadata(ImageDto imageDto) {
        // Call ImaggaService to get detected objects from image URL
        List<String> objectsInImage = imaggaService.getObjectsInImage(imageDto.getUrl());

        // Generate a label if not provided
        String label = (imageDto.getLabel() == null || imageDto.getLabel().isEmpty())
                ? ImageLabelGenerator.generateLabel(imageDto.getUrl(), objectsInImage)
                : imageDto.getLabel();

        Image image = new Image(imageDto.getUrl(), label, objectsInImage);
        Image savedImage = imageRepository.save(image);

        return new ImageDto(savedImage.getId(), savedImage.getUrl(), savedImage.getLabel(), savedImage.getObjectsInImage());
    }

    // Get all images in the database and convert to ImageDto
    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get image by ID and convert to ImageDto
    public Optional<ImageDto> getImageById(Long imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        return image.map(this::convertToDto);
    }

    // Get images by objects in the image (search query) and convert to ImageDto
    public List<ImageDto> getImagesByObjects(String objects) {
        // Split the comma-separated string into a list and trim each item
        List<String> objectList = Arrays.stream(objects.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Image> images = imageRepository.findImagesContainingObjects(objectList);
        return images.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Convert Image entity to ImageDto
    private ImageDto convertToDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getUrl(),
                image.getLabel(),
                image.getObjectsInImage()
        );
    }
}
