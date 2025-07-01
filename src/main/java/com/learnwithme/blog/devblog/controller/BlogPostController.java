package com.learnwithme.blog.devblog.controller;

import com.learnwithme.blog.devblog.exception.BadRequestException;
import com.learnwithme.blog.devblog.service.ArticleGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


import com.learnwithme.blog.devblog.dto.ApiResponseDto;
import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.dto.PageResponseDto;
import com.learnwithme.blog.devblog.service.BlogPostService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/posts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    @Autowired
    private ArticleGenerationService articleGenerationService;
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    /**
     * Get all published blog posts with pagination
     * @param pageNumber page number (default 0)
     * @param pageSize page size (default 10)
     * @return paginated blog posts
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlogPostDto>>> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDto<BlogPostDto> postResponse = blogPostService.getAllPosts(pageNumber, pageSize);

        ApiResponseDto<PageResponseDto<BlogPostDto>> response = ApiResponseDto.success(
                postResponse,
                "Blog posts retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get blog post by ID
     * @param postId blog post ID
     * @return blog post
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> getPostById(@PathVariable String postId) {
        BlogPostDto postDto = blogPostService.getPostById(postId);

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                postDto,
                "Blog post retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get blog post by slug
     * @param slug blog post slug
     * @return blog post
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> getPostBySlug(@PathVariable String slug) {
        BlogPostDto postDto = blogPostService.getPostBySlug(slug);

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                postDto,
                "Blog post retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a new blog post (admin only)
     * @param blogPostDto blog post data
     * @return created blog post
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> createPost(
            @RequestPart("blogPost") @Valid BlogPostDto blogPostDto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        BlogPostDto createdPost = blogPostService.createPost(blogPostDto, imageFile);

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                createdPost,
                "Blog post created successfully",
                HttpStatus.CREATED.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Update blog post (admin only)
     * @param postId blog post ID
     * @param blogPostDto updated blog post data
     * @return updated blog post
     */
    @PutMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> updatePost(
            @PathVariable String postId,
            @Valid @RequestBody BlogPostDto blogPostDto) {

        BlogPostDto updatedPost = blogPostService.updatePost(postId, blogPostDto);

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                updatedPost,
                "Blog post updated successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete blog post (admin only)
     * @param postId blog post ID
     * @return success message
     */
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Object>> deletePost(@PathVariable String postId) {
        blogPostService.deletePost(postId);

        ApiResponseDto<Object> response = ApiResponseDto.success(
                "Blog post deleted successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get blog posts by category
     * @param categoryId category ID
     * @param pageNumber page number
     * @param pageSize page size
     * @return paginated blog posts for the category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlogPostDto>>> getPostsByCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDto<BlogPostDto> postResponse = blogPostService.getPostsByCategory(categoryId, pageNumber, pageSize);

        ApiResponseDto<PageResponseDto<BlogPostDto>> response = ApiResponseDto.success(
                postResponse,
                "Blog posts for category retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get blog posts by subcategory
     * @param categoryId category ID
     * @param subcategorySlug subcategory slug
     * @param pageNumber page number
     * @param pageSize page size
     * @return paginated blog posts for the subcategory
     */
    @GetMapping("/category/{categoryId}/subcategory/{subcategorySlug}")
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlogPostDto>>> getPostsBySubcategory(
            @PathVariable String categoryId,
            @PathVariable String subcategorySlug,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDto<BlogPostDto> postResponse = blogPostService.getPostsBySubcategory(
                categoryId, subcategorySlug, pageNumber, pageSize);

        ApiResponseDto<PageResponseDto<BlogPostDto>> response = ApiResponseDto.success(
                postResponse,
                "Blog posts for subcategory retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Search blog posts by title
     * @param term search term
     * @param pageNumber page number
     * @param pageSize page size
     * @return paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlogPostDto>>> searchPosts(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDto<BlogPostDto> postResponse = blogPostService.searchPosts(term, pageNumber, pageSize);

        ApiResponseDto<PageResponseDto<BlogPostDto>> response = ApiResponseDto.success(
                postResponse,
                "Search results retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get blog posts by tag
     * @param tag tag name
     * @param pageNumber page number
     * @param pageSize page size
     * @return paginated blog posts for the tag
     */
    @GetMapping("/tag/{tag}")
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlogPostDto>>> getPostsByTag(
            @PathVariable String tag,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        PageResponseDto<BlogPostDto> postResponse = blogPostService.getPostsByTag(tag, pageNumber, pageSize);

        ApiResponseDto<PageResponseDto<BlogPostDto>> response = ApiResponseDto.success(
                postResponse,
                "Blog posts for tag retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get recent blog posts
     * @return recent blog posts
     */
    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDto<BlogPostDto[]>> getRecentPosts() {
        BlogPostDto[] recentPosts = blogPostService.getRecentPosts();

        ApiResponseDto<BlogPostDto[]> response = ApiResponseDto.success(
                recentPosts,
                "Recent blog posts retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Publish or unpublish a blog post (admin only)
     * @param postId blog post ID
     * @param publish publish status
     * @return updated blog post
     */
    @PutMapping("/{postId}/publish")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> togglePublishStatus(
            @PathVariable String postId,
            @RequestParam boolean publish) {

        BlogPostDto updatedPost = blogPostService.togglePublishStatus(postId, publish);

        String message = publish ? "Blog post published successfully" : "Blog post unpublished successfully";

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                updatedPost,
                message,
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * Upload an image file to the blog image storage.
     *
     * @param file the MultipartFile image to upload
     * @return ResponseEntity with the URL of the uploaded image as a string
     */
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = blogPostService.uploadImage(file);
        return ResponseEntity.ok(imageUrl);
    }


    @PostMapping("/generate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<BlogPostDto>> generatePostFromCategory(@RequestBody Map<String, String> payload) {
        String categoryName = payload.get("categoryName");
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new BadRequestException("categoryName is required.");
        }

        BlogPostDto generatedPost = articleGenerationService.createGeneratedPost(categoryName);

        ApiResponseDto<BlogPostDto> response = ApiResponseDto.success(
                generatedPost,
                "AI-generated post has been created as a draft.",
                HttpStatus.CREATED.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
