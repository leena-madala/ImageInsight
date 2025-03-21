package com.leena.imageinsight.externalapi;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImaggaService {

    private final ImaggaApiClient imaggaApiClient;

    private ImaggaService(ImaggaApiClient imaggaApiClient) {
        this.imaggaApiClient = imaggaApiClient;
    }

    // Fetch objects detected (tags) from Imagga API
    public List<String> getObjectsInImage(String imageUrl) {
        ImaggaTagsResponse response = imaggaApiClient.getTags(imageUrl);

        // Handle invalid or empty responses
        if (response == null || response.getResult() == null || response.getResult().getTags() == null) {
            return Collections.emptyList();
        }

        // Convert the API response to a list of detected objects (tags)
        return response.getResult().getTags().stream()
                .filter(tag -> tag != null && tag.getTag() != null && tag.getTag().getEn() != null)
                .map(tag -> tag.getTag().getEn())
                .collect(Collectors.toList());
    }
}
