package com.learnwithme.blog.devblog.service;

import java.util.List;

import com.learnwithme.blog.devblog.dto.CategoryDto;
import com.learnwithme.blog.devblog.dto.SubcategoryDto;

public interface CategoryService {

    /**
     * Create a new category
     * @param categoryDto the category data
     * @return the created category
     */
    CategoryDto createCategory(CategoryDto categoryDto);

    /**
     * Add a subcategory to an existing category
     * @param categoryId the ID of the category
     * @param subcategoryDto the subcategory data
     * @return the updated category
     */
    CategoryDto addSubcategory(String categoryId, SubcategoryDto subcategoryDto);

    /**
     * Get all categories with their subcategories
     * @return list of all categories
     */
    List<CategoryDto> getAllCategories();

    /**
     * Get a category by its ID
     * @param categoryId the ID of the category
     * @return the category if found
     */
    CategoryDto getCategoryById(String categoryId);

    /**
     * Get a category by its slug
     * @param slug the slug of the category
     * @return the category if found
     */
    CategoryDto getCategoryBySlug(String slug);

    /**
     * Update a category
     * @param categoryId the ID of the category to update
     * @param categoryDto the updated category data
     * @return the updated category
     */
    CategoryDto updateCategory(String categoryId, CategoryDto categoryDto);

    /**
     * Delete a category
     * @param categoryId the ID of the category to delete
     */
    void deleteCategory(String categoryId);
}