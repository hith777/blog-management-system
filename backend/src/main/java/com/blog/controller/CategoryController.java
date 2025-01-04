package com.blog.controller;

import com.blog.dto.CategoryDTO;
import com.blog.dto.CategoryRequest;
import com.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable @NonNull Long id) {
        try {
            CategoryDTO category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryDTO category = categoryService.createCategory(categoryRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable @NonNull Long id,
                                            @Valid @RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryDTO category = categoryService.updateCategory(id, categoryRequest);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable @NonNull Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {
    }
}

