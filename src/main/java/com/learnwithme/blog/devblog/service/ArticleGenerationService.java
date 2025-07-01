package com.learnwithme.blog.devblog.service;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.dto.GeneratedArticleContentDto;

public interface ArticleGenerationService {
    BlogPostDto createGeneratedPost(String categoryName);
}