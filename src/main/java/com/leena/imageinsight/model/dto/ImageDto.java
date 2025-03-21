package com.leena.imageinsight.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private Long id;

    @NotNull(message = "Image URL is required.")
    @Size(min = 1, message = "Image URL cannot be empty.")
    @URL(message = "Invalid URL format.")
    private String url;

    private String label;
    private List<String> objectsInImage;

    public ImageDto(String url, String label, List<String> objectsInImage) {
        this.url = url;
        this.label = label;
        this.objectsInImage = objectsInImage;
    }
}