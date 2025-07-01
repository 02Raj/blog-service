package com.learnwithme.blog.devblog.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiException {
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;
    private final int status;

    public static ApiException of(String message, String details, int status) {
        return new ApiException(message, details, LocalDateTime.now(), status);
    }
}
