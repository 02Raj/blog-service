package com.learnwithme.blog.devblog.dto;

import jakarta.validation.constraints.NotEmpty;

public class GenerateArticleRequestDto {

    @NotEmpty(message = "Category name cannot be empty")
    private String categoryName;

    // Getter and Setter
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
