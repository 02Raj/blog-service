package com.learnwithme.blog.devblog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnwithme.blog.devblog.dto.CategoryDto;
import com.learnwithme.blog.devblog.dto.SubcategoryDto;
import com.learnwithme.blog.devblog.exception.ResourceNotFoundException;
import com.learnwithme.blog.devblog.model.Category;
import com.learnwithme.blog.devblog.model.Subcategory;
import com.learnwithme.blog.devblog.repository.CategoryRepository;
import com.learnwithme.blog.devblog.service.CategoryService;
import com.learnwithme.blog.devblog.util.SlugUtil;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Generate slug if not provided
        if (categoryDto.getSlug() == null || categoryDto.getSlug().isEmpty()) {
            categoryDto.setSlug(SlugUtil.toSlug(categoryDto.getName()));
        }

        // Check if slug already exists
        if (categoryRepository.existsBySlug(categoryDto.getSlug())) {
            throw new IllegalArgumentException("Category with slug '" + categoryDto.getSlug() + "' already exists");
        }

        // Map DTO to entity
        Category category = mapToEntity(categoryDto);

        // Save category
        Category savedCategory = categoryRepository.save(category);

        // Map entity to DTO and return
        return mapToDto(savedCategory);
    }

    @Override
    public CategoryDto addSubcategory(String categoryId, SubcategoryDto subcategoryDto) {
        // Find category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        // Generate slug if not provided
        if (subcategoryDto.getSlug() == null || subcategoryDto.getSlug().isEmpty()) {
            subcategoryDto.setSlug(SlugUtil.toSlug(subcategoryDto.getName()));
        }

        // Check if subcategory with same slug already exists
        boolean subcategoryExists = category.getSubcategories().stream()
                .anyMatch(sub -> sub.getSlug().equals(subcategoryDto.getSlug()));

        if (subcategoryExists) {
            throw new IllegalArgumentException("Subcategory with slug '" + subcategoryDto.getSlug() + "' already exists in this category");
        }

        // Create new subcategory
        Subcategory subcategory = new Subcategory(subcategoryDto.getName(), subcategoryDto.getSlug());

        // Add subcategory to category
        category.addSubcategory(subcategory);

        // Save category
        Category updatedCategory = categoryRepository.save(category);

        // Map entity to DTO and return
        return mapToDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return mapToDto(category);
    }

    @Override
    public CategoryDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
        return mapToDto(category);
    }

    @Override
    public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        category.setName(categoryDto.getName());

        if (categoryDto.getSlug() != null && !categoryDto.getSlug().isEmpty()
                && !categoryDto.getSlug().equals(category.getSlug())) {
            if (categoryRepository.existsBySlug(categoryDto.getSlug())) {
                throw new IllegalArgumentException("Category with slug '" + categoryDto.getSlug() + "' already exists");
            }
            category.setSlug(categoryDto.getSlug());
        }

        // Clear existing subcategories
        category.getSubcategories().clear();

        // Map new subcategories from DTO to entity and add
        List<Subcategory> newSubs = categoryDto.getSubcategories().stream()
                .map(dto -> new Subcategory(dto.getName(), dto.getSlug()))
                .collect(Collectors.toList());

        category.getSubcategories().addAll(newSubs);

        // Save updated category
        Category updatedCategory = categoryRepository.save(category);

        return mapToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {
        // Find category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        // Delete category
        categoryRepository.delete(category);
    }

    // Helper methods for mapping
    private CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setSlug(category.getSlug());

        // Map subcategories
        List<SubcategoryDto> subcategoryDtos = category.getSubcategories().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        categoryDto.setSubcategories(subcategoryDtos);

        return categoryDto;
    }

    private SubcategoryDto mapToDto(Subcategory subcategory) {
        SubcategoryDto subcategoryDto = new SubcategoryDto();
        subcategoryDto.setName(subcategory.getName());
        subcategoryDto.setSlug(subcategory.getSlug());
        return subcategoryDto;
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setSlug(categoryDto.getSlug());

        // Map subcategories if any
        if (categoryDto.getSubcategories() != null && !categoryDto.getSubcategories().isEmpty()) {
            List<Subcategory> subcategories = categoryDto.getSubcategories().stream()
                    .map(this::mapToEntity)
                    .collect(Collectors.toList());
            category.setSubcategories(subcategories);
        }

        return category;
    }

    private Subcategory mapToEntity(SubcategoryDto subcategoryDto) {
        return new Subcategory(subcategoryDto.getName(), subcategoryDto.getSlug());
    }
}