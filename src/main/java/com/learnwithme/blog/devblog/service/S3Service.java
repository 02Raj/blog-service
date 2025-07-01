package com.learnwithme.blog.devblog.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file, String folder);
    void deleteFile(String fileUrl);
}
