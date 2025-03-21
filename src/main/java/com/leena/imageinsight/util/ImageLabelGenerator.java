package com.leena.imageinsight.util;

import java.net.URL;
import java.util.List;

public class ImageLabelGenerator {

    private static final String TLD_REGEX = "\\.(com|org|net|gov)$";
    private static final String DIGIT_REGEX = "\\d+";
    private static final String DOMAIN_REGEX = "www\\.";

    public static String generateLabel(String imageUrl, List<String> imageObjects) {
        String domain = extractDomain(imageUrl);
        String labelComponent = createLabelComponent(imageUrl, imageObjects);

        // Return formatted label
        return formatLabel(domain, labelComponent);
    }

    public static String extractDomain(String imageUrl) {
        String domain = "";

        try {
            URL url = new URL(imageUrl);
            domain = url.getHost().replaceFirst(DOMAIN_REGEX, ""); // Remove "www." if present
            domain = domain.replaceAll(TLD_REGEX, "");  // Remove common TLDs
        } catch (Exception e) {
            // Ignore malformed URL and proceed with fallback logic (empty domain)
        }

        return domain;
    }

    public static String createLabelComponent(String imageUrl, List<String> imageObjects) {
        if (imageObjects != null && !imageObjects.isEmpty()) {
            return formatLabel(imageObjects.get(0)); // Use first detected object
        } else {
            return formatLabelFromImageName(imageUrl); // Use image name if no detected objects
        }
    }

    public static String formatLabelFromImageName(String imageUrl) {
        String imageName = extractImageName(imageUrl);
        return formatLabel(imageName); // Format the image name
    }

    public static String extractImageName(String imageUrl) {
        // Extract image name from URL (without extension)
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1, imageUrl.lastIndexOf('.'));
    }

    public static String formatLabel(String text) {
        // Handle cases with empty text or text that's just made of digits
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Clean up: Remove digits, replace hyphens with underscores, and replace multiple underscores with one
        text = text.toLowerCase()
                .replace(" ", "_")
                .replaceAll(DIGIT_REGEX, "")  // Remove digits
                .replace("-", "_")  // Replace hyphens with underscores
                .replaceAll("_+", "_");  // Replace multiple underscores with a single one

        // Clean up leading/trailing underscores or hyphens
        return text.replaceAll("^[_-]+", "").replaceAll("[_-]+$", "");
    }

    public static String formatLabel(String domain, String labelComponent) {
        // If both domain and label are non-empty, combine them with an underscore
        if (!domain.isEmpty() && !labelComponent.isEmpty()) {
            return domain + "_" + labelComponent;
        } else if (!domain.isEmpty()) {
            // If only domain is non-empty, return the domain itself
            return domain;
        } else if (!labelComponent.isEmpty()) {
            // If only label component is non-empty, return the label component itself
            return labelComponent;
        } else {
            // If both are empty, return an empty string
            return "";
        }
    }
}