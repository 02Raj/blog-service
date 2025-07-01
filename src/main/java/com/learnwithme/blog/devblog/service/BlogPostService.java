package com.learnwithme.blog.devblog.service;

import com.learnwithme.blog.devblog.dto.BlogPostDto;
import com.learnwithme.blog.devblog.dto.PageResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface BlogPostService {

    /**
     * Create a new blog post
     * @param blogPostDto the blog post data
     * @return the created blog post
     */
    BlogPostDto createPost(BlogPostDto blogPostDto, MultipartFile imageFile);


    /**
     * Get all blog posts with pagination
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return paginated blog posts
     */
    PageResponseDto<BlogPostDto> getAllPosts(int pageNumber, int pageSize);

    /**
     * Get a blog post by its ID
     * @param postId the ID of the blog post
     * @return the blog post if found
     */
    BlogPostDto getPostById(String postId);

    /**
     * Get a blog post by its slug
     * @param slug the slug of the blog post
     * @return the blog post if found
     */
    BlogPostDto getPostBySlug(String slug);

    /**
     * Update a blog post
     * @param postId the ID of the blog post to update
     * @param blogPostDto the updated blog post data
     * @return the updated blog post
     */
    BlogPostDto updatePost(String postId, BlogPostDto blogPostDto);

    /**
     * Delete a blog post
     * @param postId the ID of the blog post to delete
     */
    void deletePost(String postId);

    /**
     * Get blog posts by category with pagination
     * @param categoryId the ID of the category
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return paginated blog posts for the category
     */
    PageResponseDto<BlogPostDto> getPostsByCategory(String categoryId, int pageNumber, int pageSize);

    /**
     * Get blog posts by subcategory with pagination
     * @param categoryId the ID of the category
     * @param subcategorySlug the slug of the subcategory
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return paginated blog posts for the subcategory
     */
    PageResponseDto<BlogPostDto> getPostsBySubcategory(String categoryId, String subcategorySlug, int pageNumber, int pageSize);

    /**
     * Search for blog posts by title
     * @param searchTerm the search term
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return paginated search results
     */
    PageResponseDto<BlogPostDto> searchPosts(String searchTerm, int pageNumber, int pageSize);

    /**
     * Get blog posts by tag with pagination
     * @param tag the tag
     * @param pageNumber the page number
     * @param pageSize the page size
     * @return paginated blog posts for the tag
     */
    PageResponseDto<BlogPostDto> getPostsByTag(String tag, int pageNumber, int pageSize);

    /**
     * Get recent blog posts
     * @return list of recent blog posts
     */
    BlogPostDto[] getRecentPosts();

    /**
     * Publish or unpublish a blog post
     * @param postId the ID of the blog post
     * @param publish true to publish, false to unpublish
     * @return the updated blog post
     */
    BlogPostDto togglePublishStatus(String postId, boolean publish);

    /**
     * Upload an image file to S3 and get the URL
     * @param file MultipartFile to upload
     * @return URL of the uploaded image
     */
    String uploadImage(MultipartFile file);

    /**
     * Generates and saves a new blog post for a given category as a draft.
     * @param categoryName the name of the category for the article.
     * @return the created blog post, saved as a draft.
     */
//    BlogPostDto createGeneratedPost(String categoryName);

}
