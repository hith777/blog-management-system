package com.blog.service;

import com.blog.dto.CategoryDTO;
import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(@NonNull Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(@NonNull Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!category.getName().equals(categoryRequest.getName()) &&
            categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        return convertToDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(@NonNull Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check if category has posts
        if (!category.getPosts().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing posts");
        }

        categoryRepository.delete(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }
}

