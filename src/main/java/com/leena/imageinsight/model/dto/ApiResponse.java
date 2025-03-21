package com.leena.imageinsight.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private int status;
    private String message;
    private String timestamp;
    private Object data; // For success responses, optional
    private List<String> errors; // For error responses, optional

    // Constructor for error responses
    public ApiResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = getCurrentTimestamp();
    }

    // Constructor for success responses
    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = getCurrentTimestamp();
    }

    // Helper method to get current timestamp in readable format
    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
