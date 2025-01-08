package com.blog.controller;

import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.security.JwtTokenProvider;
import com.blog.security.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private User author;
    private Category category;
    private Tag tag1;
    private String authToken;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        author = new User();
        author.setUsername("author");
        author.setEmail("author@example.com");
        author.setPassword(passwordEncoder.encode("password123"));
        author = userRepository.save(author);

        category = new Category();
        category.setName("Technology");
        category.setDescription("Tech posts");
        category = categoryRepository.save(category);

        tag1 = new Tag();
        tag1.setName("Java");
        tag1 = tagRepository.save(tag1);

        // Generate JWT token
        UserPrincipal userPrincipal = UserPrincipal.create(author);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        authToken = tokenProvider.generateToken(authentication);
    }

    @Test
    void testGetAllPosts() throws Exception {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        postRepository.save(post);

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Post"));
    }

    @Test
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setCategory(category);
        Post saved = postRepository.save(post);

        mockMvc.perform(get("/api/posts/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.categoryId").value(category.getId()));
    }

    @Test
    void testGetPostByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePost() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new PostRequest(
                "New Post", "New Content", category.getId(), Set.of(tag1.getId())
        ));

        mockMvc.perform(post("/api/posts")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Post"))
                .andExpect(jsonPath("$.content").value("New Content"))
                .andExpect(jsonPath("$.authorId").value(author.getId()));
    }

    @Test
    void testCreatePostUnauthorized() throws Exception {
        String requestBody = objectMapper.writeValueAsString(new PostRequest(
                "New Post", "New Content", null, null
        ));

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdatePost() throws Exception {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        String requestBody = objectMapper.writeValueAsString(new PostRequest(
                "Updated Title", "Updated Content", category.getId(), Set.of(tag1.getId())
        ));

        mockMvc.perform(put("/api/posts/{id}", saved.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));
    }

    @Test
    void testDeletePost() throws Exception {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        mockMvc.perform(delete("/api/posts/{id}", saved.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNoContent());
    }

    // Helper record class for request body
    private record PostRequest(String title, String content, Long categoryId, Set<Long> tagIds) {}
}

