package com.leena.imageinsight.externalapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leena.imageinsight.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Component
@Slf4j
class ImaggaApiClient {

    @Value("${imagga.api.url}")
    private String imaggaApiUrl;

    @Value("${imagga.api.key}")
    private String imaggaApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ImaggaApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ImaggaTagsResponse getTags(String imageUrl) {
        try {
            URI uri = new URI(imaggaApiUrl + "?image_url=" + imageUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodeApiKey(imaggaApiKey));
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, uri);

            // Make the request and get the raw response as a String
            ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
            String responseBody = responseEntity.getBody();

            if (responseBody == null || responseBody.isEmpty()) {
                throw new ApiException(500, "No response from external API Imagga.");
            }

            // Print the raw JSON response for debugging
            log.debug("Imagga API Raw Response: {}", responseBody);

            // Convert the raw response body to ImaggaTagsResponse
            return objectMapper.readValue(responseBody, ImaggaTagsResponse.class);

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // Handle HTTP error responses from the external API
            // Parse the error message from Imagga API's response
            int statusCode = ex.getStatusCode().value();
            String errorResponse = ex.getResponseBodyAsString();
            String errorMessage = parseErrorMessage(errorResponse);

            // Rethrow Imaaga exception
            throw new ApiException(statusCode, "An error occurred: " + errorMessage);
        } catch (Exception e) {
            // Rethrow Imaaga exception with 500 status code
            throw new ApiException(500, "Internal Server Error: " + e.getMessage());
        }
    }

    private String encodeApiKey(String apiKey) {
        return java.util.Base64.getEncoder().encodeToString(apiKey.getBytes());
    }

    private String parseErrorMessage(String errorResponse) {
        try {
            // Parse the error response JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(errorResponse);
            JsonNode statusNode = rootNode.get("status");

            // Extract the 'text' field from the 'status' object
            if (statusNode != null && statusNode.has("text")) {
                return statusNode.get("text").asText();
            }

            // Fallback to a generic message if 'text' is not found
            return "An error occurred";
        } catch (Exception e) {
            // In case JSON parsing fails, return a generic error message
            return "Failed to parse error message from the API response";
        }
    }
}