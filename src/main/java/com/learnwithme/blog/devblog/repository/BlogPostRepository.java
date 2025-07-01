package com.learnwithme.blog.devblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.learnwithme.blog.devblog.model.BlogPost;

public interface BlogPostRepository extends MongoRepository<BlogPost, String> {

    /**
     * Find a blog post by its slug
     * @param slug the slug
     * @return the blog post if found
     */
    Optional<BlogPost> findBySlug(String slug);

    /**
     * Check if a blog post with the given slug exists
     * @param slug the slug
     * @return true if exists, false otherwise
     */
    boolean existsBySlug(String slug);

    /**
     * Find all published blog posts
     * @param pageable pagination information
     * @return page of published blog posts
     */
    Page<BlogPost> findByPublishedTrue(Pageable pageable);

    /**
     * Find published blog posts by category
     * @param categoryId the category ID
     * @param pageable pagination information
     * @return page of published blog posts for the category
     */
    Page<BlogPost> findByCategoryIdAndPublishedTrue(String categoryId, Pageable pageable);

    /**
     * Find published blog posts by category and subcategory
     * @param categoryId the category ID
     * @param subcategorySlug the subcategory slug
     * @param pageable pagination information
     * @return page of published blog posts for the subcategory
     */
    Page<BlogPost> findByCategoryIdAndSubcategorySlugAndPublishedTrue(String categoryId, String subcategorySlug, Pageable pageable);

    /**
     * Find published blog posts by tag
     * @param tag the tag
     * @param pageable pagination information
     * @return page of published blog posts for the tag
     */
    @Query("{'tags': ?0, 'published': true}")
    Page<BlogPost> findByTagAndPublishedTrue(String tag, Pageable pageable);

    /**
     * Search published blog posts by title (case-insensitive)
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return page of published blog posts matching the search term
     */
    @Query("{'title': {$regex: ?0, $options: 'i'}, 'published': true}")
    Page<BlogPost> searchByTitleAndPublishedTrue(String searchTerm, Pageable pageable);

    /**
     * Find top 5 most recent blog posts
     * @return list of recent blog posts
     */
    List<BlogPost> findTop5ByPublishedTrueOrderByCreatedAtDesc();

    /**
     * Find all blog posts by author
     * @param author the author
     * @param pageable pagination information
     * @return page of blog posts by the author
     */
    Page<BlogPost> findByAuthorAndPublishedTrue(String author, Pageable pageable);
}
