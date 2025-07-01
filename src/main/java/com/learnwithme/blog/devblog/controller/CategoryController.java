package com.learnwithme.blog.devblog.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.learnwithme.blog.devblog.dto.ApiResponseDto;
import com.learnwithme.blog.devblog.dto.CategoryDto;
import com.learnwithme.blog.devblog.dto.SubcategoryDto;
import com.learnwithme.blog.devblog.service.CategoryService;

import jakarta.validation.Valid;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get all categories
     * @return list of all categories
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();

        ApiResponseDto<List<CategoryDto>> response = ApiResponseDto.success(
                categories,
                "Categories retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get category by ID
     * @param categoryId category ID
     * @return category
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponseDto<CategoryDto>> getCategoryById(@PathVariable String categoryId) {
        CategoryDto category = categoryService.getCategoryById(categoryId);

        ApiResponseDto<CategoryDto> response = ApiResponseDto.success(
                category,
                "Category retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get category by slug
     * @param slug category slug
     * @return category
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponseDto<CategoryDto>> getCategoryBySlug(@PathVariable String slug) {
        CategoryDto category = categoryService.getCategoryBySlug(slug);

        ApiResponseDto<CategoryDto> response = ApiResponseDto.success(
                category,
                "Category retrieved successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a new category (admin only)
     * @param categoryDto category data
     * @return created category
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<CategoryDto>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);

        ApiResponseDto<CategoryDto> response = ApiResponseDto.success(
                createdCategory,
                "Category created successfully",
                HttpStatus.CREATED.value()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Add a subcategory to a category (admin only)
     * @param categoryId category ID
     * @param subcategoryDto subcategory data
     * @return updated category
     */
    @PostMapping("/{categoryId}/subcategories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<CategoryDto>> addSubcategory(
            @PathVariable String categoryId,
            @Valid @RequestBody SubcategoryDto subcategoryDto) {

        CategoryDto updatedCategory = categoryService.addSubcategory(categoryId, subcategoryDto);

        ApiResponseDto<CategoryDto> response = ApiResponseDto.success(
                updatedCategory,
                "Subcategory added successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update a category (admin only)
     * @param categoryId category ID
     * @param categoryDto updated category data
     * @return updated category
     */
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<CategoryDto>> updateCategory(
            @PathVariable String categoryId,
            @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);

        ApiResponseDto<CategoryDto> response = ApiResponseDto.success(
                updatedCategory,
                "Category updated successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a category (admin only)
     * @param categoryId category ID
     * @return success message
     */
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<Object>> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);

        ApiResponseDto<Object> response = ApiResponseDto.success(
                "Category deleted successfully",
                HttpStatus.OK.value()
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}