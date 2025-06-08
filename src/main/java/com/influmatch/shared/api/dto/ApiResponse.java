package com.influmatch.shared.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
    }

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
        this.success = true;
    }
} 