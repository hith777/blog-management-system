package com.blog.controller;

import com.blog.dto.PostDTO;
import com.blog.dto.PostRequest;
import com.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable @NonNull Long id) {
        try {
            PostDTO post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        try {
            PostDTO post = postService.createPost(postRequest, currentUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable @NonNull Long id,
                                        @Valid @RequestBody PostRequest postRequest,
                                        @AuthenticationPrincipal UserDetails currentUser) {
        try {
            PostDTO post = postService.updatePost(id, postRequest, currentUser);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable @NonNull Long id,
                                         @AuthenticationPrincipal UserDetails currentUser) {
        try {
            postService.deletePost(id, currentUser);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {
    }
}

