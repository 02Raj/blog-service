package com.learnwithme.blog.devblog.util;

import com.learnwithme.blog.devblog.service.S3Service;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class S3Utils {

    private final S3Service s3Service;

    public S3Utils(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Generate a pre-signed URL for temporary file access
     */
    public String generatePresignedUrl(String fileKey, Duration expiration) {
        // Implementation for pre-signed URLs if needed
        return "";
    }

    /**
     * Check if file exists in S3
     */
    public boolean fileExists(String fileUrl) {
        // Implementation to check file existence
        return false;
    }
}
