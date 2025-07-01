package com.learnwithme.blog.devblog.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)  // Don't include null fields in JSON response
public class ApiResponseDto<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private int status;

    /**
     * Create success response with data
     * @param <T> Type of response data
     * @param data Data to include in response
     * @param message Success message
     * @param status HTTP status code
     * @return ApiResponseDto instance
     */
    public static <T> ApiResponseDto<T> success(T data, String message, int status) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Create simple success response without data
     * @param message Success message
     * @param status HTTP status code
     * @return ApiResponseDto instance with null data
     */
    public static ApiResponseDto<Object> success(String message, int status) {
        return ApiResponseDto.builder()
                .success(true)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Create error response
     * @param message Error message
     * @param status HTTP status code
     * @return ApiResponseDto instance
     */
    public static ApiResponseDto<Object> error(String message, int status) {
        return ApiResponseDto.builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }
}