package com.learnwithme.blog.devblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.learnwithme.blog.devblog.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

    /**
     * Find a category by its slug
     * @param slug the slug
     * @return the category if found
     */
    Optional<Category> findBySlug(String slug);

    /**
     * Check if a category with the given slug exists
     * @param slug the slug
     * @return true if exists, false otherwise
     */
    boolean existsBySlug(String slug);

    /**
     * Find all categories ordered by name in ascending order
     * @return list of all categories
     */
    List<Category> findAllByOrderByNameAsc();
}
