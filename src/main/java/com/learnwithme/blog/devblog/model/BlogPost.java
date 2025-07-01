package com.learnwithme.blog.devblog.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "blog_posts")
public class BlogPost {

    @Id
    private String id;

    private String title;

    private String slug;

    private String content;

    private String categoryId;

    private String categoryName;

    private String subcategorySlug;

    private String subcategoryName;

    private String author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String imageUrl;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private boolean published = false;

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}