package com.learnwithme.blog.devblog.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPostDto {

    private String id;
    private String title;
    private String slug;
    private String content;
    private String summary; //
    private String categoryId;
    private String categoryName;
    private String subcategorySlug;
    private String subcategoryName;
    private String imageUrl;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private boolean published = false;
}
