package com.blog.service;

import com.blog.dto.PostDTO;
import com.blog.dto.PostRequest;
import com.blog.entity.*;
import com.blog.repository.PostRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        return postRepository.findAllWithRelations().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDTO getPostById(@NonNull Long id) {
        Post post = postRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return convertToDTO(post);
    }

    @Transactional
    public PostDTO createPost(PostRequest postRequest, UserDetails currentUser) {
        User author = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(author);

        if (postRequest.getCategoryId() != null) {
            Long categoryId = Objects.requireNonNull(postRequest.getCategoryId());
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        }

        if (postRequest.getTagIds() != null) {
            if (!postRequest.getTagIds().isEmpty()) {
                // Set tags when non-empty set is provided
                Set<Tag> tags = postRequest.getTagIds().stream()
                        .map(tagId -> tagRepository.findById(Objects.requireNonNull(tagId))
                                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId)))
                        .collect(Collectors.toSet());
                post.setTags(tags);
            }
            // If tagIds is empty, post starts with no tags (default behavior)
        }
        // If tagIds is null, post starts with no tags (default behavior)

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    @Transactional
    public PostDTO updatePost(@NonNull Long id, PostRequest postRequest, UserDetails currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        User author = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is the author or admin
        if (!post.getAuthor().getId().equals(author.getId()) && 
            !currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        if (postRequest.getCategoryId() != null) {
            Long categoryId = Objects.requireNonNull(postRequest.getCategoryId());
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (postRequest.getTagIds() != null) {
            if (postRequest.getTagIds().isEmpty()) {
                // Clear tags when empty set is provided
                post.setTags(new java.util.HashSet<>());
            } else {
                // Set tags when non-empty set is provided
                Set<Tag> tags = postRequest.getTagIds().stream()
                        .map(tagId -> tagRepository.findById(Objects.requireNonNull(tagId))
                                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId)))
                        .collect(Collectors.toSet());
                post.setTags(tags);
            }
        }
        // If tagIds is null, leave existing tags unchanged

        Post updatedPost = postRepository.save(post);
        return convertToDTO(updatedPost);
    }

    @Transactional
    public void deletePost(@NonNull Long id, UserDetails currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        User author = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user is the author or admin
        if (!post.getAuthor().getId().equals(author.getId()) && 
            !currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }
        dto.setTags(post.getTags().stream()
                .map(tag -> new com.blog.dto.TagDTO(tag.getId(), tag.getName(), tag.getCreatedAt(), tag.getUpdatedAt()))
                .collect(Collectors.toSet()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}

