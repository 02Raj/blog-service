package com.learnwithme.blog.devblog.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cloudinary.Cloudinary;
import com.learnwithme.blog.devblog.service.ArticleGenerationService;
import com.learnwithme.blog.devblog.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.dto.PageResponseDto;
import com.learnwithme.blog.devblog.exception.ResourceNotFoundException;
import com.learnwithme.blog.devblog.model.BlogPost;
import com.learnwithme.blog.devblog.model.Category;
import com.learnwithme.blog.devblog.model.Subcategory;
import com.learnwithme.blog.devblog.repository.BlogPostRepository;
import com.learnwithme.blog.devblog.repository.CategoryRepository;
import com.learnwithme.blog.devblog.service.BlogPostService;
import com.learnwithme.blog.devblog.util.SlugUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BlogPostServiceImpl implements BlogPostService  {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private  S3Service s3Service;
//    @Autowired
//    private ArticleGenerationService articleGenerationService;

    @Override
    public BlogPostDto createPost(BlogPostDto blogPostDto, MultipartFile imageFile) {
        // Generate slug if not provided
        if (blogPostDto.getSlug() == null || blogPostDto.getSlug().isEmpty()) {
            blogPostDto.setSlug(SlugUtil.toSlug(blogPostDto.getTitle()));
        }

        // Check if slug already exists
        if (blogPostRepository.existsBySlug(blogPostDto.getSlug())) {
            throw new IllegalArgumentException("Blog post with slug '" + blogPostDto.getSlug() + "' already exists");
        }

        // Handle category and subcategory
        if (blogPostDto.getCategoryId() != null && !blogPostDto.getCategoryId().isEmpty()) {
            Category category = categoryRepository.findById(blogPostDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", blogPostDto.getCategoryId()));

            blogPostDto.setCategoryName(category.getName());

            if (blogPostDto.getSubcategorySlug() != null && !blogPostDto.getSubcategorySlug().isEmpty()) {
                Subcategory subcategory = category.getSubcategories().stream()
                        .filter(sub -> sub.getSlug().equals(blogPostDto.getSubcategorySlug()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "slug", blogPostDto.getSubcategorySlug()));

                blogPostDto.setSubcategoryName(subcategory.getName());
            }
        }

        // Upload image to S3 (Replace Cloudinary logic)
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadFile(imageFile, "blog-images");
                blogPostDto.setImageUrl(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        blogPostDto.setCreatedAt(now);
        blogPostDto.setUpdatedAt(now);

        // Map to entity
        BlogPost blogPost = mapToEntity(blogPostDto);

        // Save to DB
        BlogPost saved = blogPostRepository.save(blogPost);

        return mapToDto(saved);
    }


    @Override
    public PageResponseDto<BlogPostDto> getAllPosts(int pageNumber, int pageSize) {
        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        // Get page of blog posts
        Page<BlogPost> blogPostsPage = blogPostRepository.findByPublishedTrue(pageable);

        // Map to DTOs
        List<BlogPostDto> blogPostDtos = blogPostsPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Create page response DTO
        PageResponseDto<BlogPostDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(blogPostDtos);
        pageResponseDto.setPageNumber(blogPostsPage.getNumber());
        pageResponseDto.setPageSize(blogPostsPage.getSize());
        pageResponseDto.setTotalElements(blogPostsPage.getTotalElements());
        pageResponseDto.setTotalPages(blogPostsPage.getTotalPages());
        pageResponseDto.setLast(blogPostsPage.isLast());

        return pageResponseDto;
    }

    @Override
    public BlogPostDto getPostById(String postId) {
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", postId));
        return mapToDto(blogPost);
    }

    @Override
    public BlogPostDto getPostBySlug(String slug) {
        BlogPost blogPost = blogPostRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "slug", slug));
        return mapToDto(blogPost);
    }

    @Override
    public BlogPostDto updatePost(String postId, BlogPostDto blogPostDto) {
        // Find blog post
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", postId));

        // Update basic fields
        if (blogPostDto.getTitle() != null) {
            blogPost.setTitle(blogPostDto.getTitle());
        }

        if (blogPostDto.getContent() != null) {
            blogPost.setContent(blogPostDto.getContent());
        }

        if (blogPostDto.getTags() != null) {
            blogPost.setTags(blogPostDto.getTags());
        }

        blogPost.setUpdatedAt(LocalDateTime.now());

        // Update slug if provided and different
        if (blogPostDto.getSlug() != null && !blogPostDto.getSlug().isEmpty()
                && !blogPostDto.getSlug().equals(blogPost.getSlug())) {

            // Check if new slug already exists
            if (blogPostRepository.existsBySlug(blogPostDto.getSlug())) {
                throw new IllegalArgumentException("Blog post with slug '" + blogPostDto.getSlug() + "' already exists");
            }

            blogPost.setSlug(blogPostDto.getSlug());
        }

        // Update category and subcategory information if provided
        if (blogPostDto.getCategoryId() != null && !blogPostDto.getCategoryId().isEmpty()
                && !blogPostDto.getCategoryId().equals(blogPost.getCategoryId())) {

            Category category = categoryRepository.findById(blogPostDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", blogPostDto.getCategoryId()));

            blogPost.setCategoryId(category.getId());
            blogPost.setCategoryName(category.getName());

            // Reset subcategory info if category changed
            blogPost.setSubcategorySlug(null);
            blogPost.setSubcategoryName(null);
        }

        // Update subcategory information if provided
        if (blogPostDto.getSubcategorySlug() != null
                && !blogPostDto.getSubcategorySlug().equals(blogPost.getSubcategorySlug())) {

            Category category = categoryRepository.findById(blogPost.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", blogPost.getCategoryId()));

            // Find subcategory
            Subcategory subcategory = category.getSubcategories().stream()
                    .filter(sub -> sub.getSlug().equals(blogPostDto.getSubcategorySlug()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategory", "slug", blogPostDto.getSubcategorySlug()));

            blogPost.setSubcategorySlug(subcategory.getSlug());
            blogPost.setSubcategoryName(subcategory.getName());
        }

        // Save updated blog post
        BlogPost updatedBlogPost = blogPostRepository.save(blogPost);

        // Map entity to DTO and return
        return mapToDto(updatedBlogPost);
    }

    @Override
    public void deletePost(String postId) {
        // Find blog post
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", postId));

        // Delete blog post
        blogPostRepository.delete(blogPost);
    }

    @Override
    public PageResponseDto<BlogPostDto> getPostsByCategory(String categoryId, int pageNumber, int pageSize) {
        // Verify category exists
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }

        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        // Get page of blog posts for category
        Page<BlogPost> blogPostsPage = blogPostRepository.findByCategoryIdAndPublishedTrue(categoryId, pageable);

        // Map to DTOs
        List<BlogPostDto> blogPostDtos = blogPostsPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Create page response DTO
        PageResponseDto<BlogPostDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(blogPostDtos);
        pageResponseDto.setPageNumber(blogPostsPage.getNumber());
        pageResponseDto.setPageSize(blogPostsPage.getSize());
        pageResponseDto.setTotalElements(blogPostsPage.getTotalElements());
        pageResponseDto.setTotalPages(blogPostsPage.getTotalPages());
        pageResponseDto.setLast(blogPostsPage.isLast());

        return pageResponseDto;
    }

    @Override
    public PageResponseDto<BlogPostDto> getPostsBySubcategory(String categoryId, String subcategorySlug, int pageNumber, int pageSize) {
        // Verify category exists
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        // Verify subcategory exists
        boolean subcategoryExists = category.getSubcategories().stream()
                .anyMatch(sub -> sub.getSlug().equals(subcategorySlug));

        if (!subcategoryExists) {
            throw new ResourceNotFoundException("Subcategory", "slug", subcategorySlug);
        }

        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        // Get page of blog posts for subcategory
        Page<BlogPost> blogPostsPage = blogPostRepository.findByCategoryIdAndSubcategorySlugAndPublishedTrue(
                categoryId, subcategorySlug, pageable);

        // Map to DTOs
        List<BlogPostDto> blogPostDtos = blogPostsPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Create page response DTO
        PageResponseDto<BlogPostDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(blogPostDtos);
        pageResponseDto.setPageNumber(blogPostsPage.getNumber());
        pageResponseDto.setPageSize(blogPostsPage.getSize());
        pageResponseDto.setTotalElements(blogPostsPage.getTotalElements());
        pageResponseDto.setTotalPages(blogPostsPage.getTotalPages());
        pageResponseDto.setLast(blogPostsPage.isLast());

        return pageResponseDto;
    }

    @Override
    public PageResponseDto<BlogPostDto> searchPosts(String searchTerm, int pageNumber, int pageSize) {
        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        // Search blog posts by title
        Page<BlogPost> blogPostsPage = blogPostRepository.searchByTitleAndPublishedTrue(searchTerm, pageable);

        // Map to DTOs
        List<BlogPostDto> blogPostDtos = blogPostsPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Create page response DTO
        PageResponseDto<BlogPostDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(blogPostDtos);
        pageResponseDto.setPageNumber(blogPostsPage.getNumber());
        pageResponseDto.setPageSize(blogPostsPage.getSize());
        pageResponseDto.setTotalElements(blogPostsPage.getTotalElements());
        pageResponseDto.setTotalPages(blogPostsPage.getTotalPages());
        pageResponseDto.setLast(blogPostsPage.isLast());

        return pageResponseDto;
    }

    @Override
    public PageResponseDto<BlogPostDto> getPostsByTag(String tag, int pageNumber, int pageSize) {
        // Create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        // Get page of blog posts for tag
        Page<BlogPost> blogPostsPage = blogPostRepository.findByTagAndPublishedTrue(tag, pageable);

        // Map to DTOs
        List<BlogPostDto> blogPostDtos = blogPostsPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        // Create page response DTO
        PageResponseDto<BlogPostDto> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(blogPostDtos);
        pageResponseDto.setPageNumber(blogPostsPage.getNumber());
        pageResponseDto.setPageSize(blogPostsPage.getSize());
        pageResponseDto.setTotalElements(blogPostsPage.getTotalElements());
        pageResponseDto.setTotalPages(blogPostsPage.getTotalPages());
        pageResponseDto.setLast(blogPostsPage.isLast());

        return pageResponseDto;
    }

    @Override
    public BlogPostDto[] getRecentPosts() {
        // Get recent blog posts
        List<BlogPost> recentPosts = blogPostRepository.findTop5ByPublishedTrueOrderByCreatedAtDesc();

        // Map to DTOs
        return recentPosts.stream()
                .map(this::mapToDto)
                .toArray(BlogPostDto[]::new);
    }

    @Override
    public BlogPostDto togglePublishStatus(String postId, boolean publish) {
        // Find blog post
        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post", "id", postId));

        // Set publish status
        blogPost.setPublished(publish);
        blogPost.setUpdatedAt(LocalDateTime.now());

        // Save updated blog post
        BlogPost updatedBlogPost = blogPostRepository.save(blogPost);

        // Map entity to DTO and return
        return mapToDto(updatedBlogPost);
    }

    // Helper methods for mapping between entity and DTO
    private BlogPostDto mapToDto(BlogPost blogPost) {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setId(blogPost.getId());
        blogPostDto.setTitle(blogPost.getTitle());
        blogPostDto.setSlug(blogPost.getSlug());
        blogPostDto.setContent(blogPost.getContent());
        blogPostDto.setCategoryId(blogPost.getCategoryId());
        blogPostDto.setCategoryName(blogPost.getCategoryName());
        blogPostDto.setSubcategorySlug(blogPost.getSubcategorySlug());
        blogPostDto.setSubcategoryName(blogPost.getSubcategoryName());
        blogPostDto.setAuthor(blogPost.getAuthor());
        blogPostDto.setCreatedAt(blogPost.getCreatedAt());
        blogPostDto.setUpdatedAt(blogPost.getUpdatedAt());
        blogPostDto.setTags(blogPost.getTags());
        blogPostDto.setPublished(blogPost.isPublished());
       blogPostDto.setImageUrl(blogPost.getImageUrl());
        return blogPostDto;
    }

    private BlogPost mapToEntity(BlogPostDto blogPostDto) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(blogPostDto.getTitle());
        blogPost.setSlug(blogPostDto.getSlug());
        blogPost.setContent(blogPostDto.getContent());
        blogPost.setCategoryId(blogPostDto.getCategoryId());
        blogPost.setCategoryName(blogPostDto.getCategoryName());
        blogPost.setSubcategorySlug(blogPostDto.getSubcategorySlug());
        blogPost.setSubcategoryName(blogPostDto.getSubcategoryName());
        blogPost.setAuthor(blogPostDto.getAuthor());
        blogPost.setCreatedAt(blogPostDto.getCreatedAt());
        blogPost.setUpdatedAt(blogPostDto.getUpdatedAt());
        blogPost.setTags(blogPostDto.getTags());
        blogPost.setPublished(blogPostDto.isPublished());
        blogPost.setImageUrl(blogPostDto.getImageUrl());
        // Don't set ID for new entities (ID will be generated)
        if (blogPostDto.getId() != null) {
            blogPost.setId(blogPostDto.getId());
        }

        return blogPost;
    }
    @Override
    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        try {
            return s3Service.uploadFile(file, "blog-images");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
}