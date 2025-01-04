package com.blog.controller;

import com.blog.dto.TagDTO;
import com.blog.dto.TagRequest;
import com.blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable @NonNull Long id) {
        try {
            TagDTO tag = tagService.getTagById(id);
            return ResponseEntity.ok(tag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody TagRequest tagRequest) {
        try {
            TagDTO tag = tagService.createTag(tagRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(tag);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable @NonNull Long id,
                                       @Valid @RequestBody TagRequest tagRequest) {
        try {
            TagDTO tag = tagService.updateTag(id, tagRequest);
            return ResponseEntity.ok(tag);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable @NonNull Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {
    }
}

