package com.blog.service;

import com.blog.dto.PostDTO;
import com.blog.dto.PostRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.TagRepository;
import com.blog.repository.UserRepository;
import com.blog.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    private User author;
    private User admin;
    private Category category;
    private Tag tag1;
    private Tag tag2;
    private UserDetails authorDetails;
    private UserDetails adminDetails;

    @BeforeEach
    @SuppressWarnings("null")
    void setUp() {
        postRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        author = new User();
        author.setUsername("author");
        author.setEmail("author@example.com");
        author.setPassword("password123");
        author = userRepository.save(author);

        admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword("password123");
        admin.getRoles().add(User.Role.ROLE_ADMIN);
        admin = userRepository.save(admin);

        category = new Category();
        category.setName("Technology");
        category.setDescription("Tech posts");
        category = categoryRepository.save(category);

        tag1 = new Tag();
        tag1.setName("Java");
        tag1 = tagRepository.save(tag1);

        tag2 = new Tag();
        tag2.setName("Spring");
        tag2 = tagRepository.save(tag2);

        authorDetails = UserPrincipal.create(author);
        adminDetails = UserPrincipal.create(admin);
    }

    @Test
    void testGetAllPosts() {
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Content 1");
        post1.setAuthor(author);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Content 2");
        post2.setAuthor(author);
        postRepository.save(post2);

        List<PostDTO> posts = postService.getAllPosts();

        assertEquals(2, posts.size());
    }

    @Test
    @SuppressWarnings("null")
    void testGetPostById() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(Set.of(tag1, tag2));
        Post saved = postRepository.save(post);

        PostDTO dto = postService.getPostById(saved.getId());

        assertEquals("Test Post", dto.getTitle());
        assertEquals("Test Content", dto.getContent());
        assertEquals(author.getId(), dto.getAuthorId());
        assertEquals(category.getId(), dto.getCategoryId());
        assertEquals(2, dto.getTags().size());
    }

    @Test
    void testGetPostByIdNotFound() {
        assertThrows(RuntimeException.class, () -> postService.getPostById(999L));
    }

    @Test
    void testCreatePost() {
        PostRequest request = new PostRequest();
        request.setTitle("New Post");
        request.setContent("New Content");
        request.setCategoryId(category.getId());
        request.setTagIds(Set.of(tag1.getId(), tag2.getId()));

        PostDTO dto = postService.createPost(request, authorDetails);

        assertNotNull(dto.getId());
        assertEquals("New Post", dto.getTitle());
        assertEquals("New Content", dto.getContent());
        assertEquals(author.getId(), dto.getAuthorId());
        assertEquals(category.getId(), dto.getCategoryId());
        assertEquals(2, dto.getTags().size());
    }

    @Test
    void testCreatePostWithoutCategory() {
        PostRequest request = new PostRequest();
        request.setTitle("New Post");
        request.setContent("New Content");

        PostDTO dto = postService.createPost(request, authorDetails);

        assertNotNull(dto.getId());
        assertNull(dto.getCategoryId());
    }

    @Test
    void testCreatePostWithEmptyTags() {
        PostRequest request = new PostRequest();
        request.setTitle("New Post");
        request.setContent("New Content");
        request.setTagIds(Collections.emptySet());

        PostDTO dto = postService.createPost(request, authorDetails);

        assertNotNull(dto.getId());
        assertTrue(dto.getTags().isEmpty());
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostByAuthor() {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        PostRequest request = new PostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        request.setCategoryId(category.getId());
        request.setTagIds(Set.of(tag1.getId()));

        PostDTO dto = postService.updatePost(saved.getId(), request, authorDetails);

        assertEquals("Updated Title", dto.getTitle());
        assertEquals("Updated Content", dto.getContent());
        assertEquals(category.getId(), dto.getCategoryId());
        assertEquals(1, dto.getTags().size());
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostByAdmin() {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        PostRequest request = new PostRequest();
        request.setTitle("Updated by Admin");
        request.setContent("Updated Content");

        PostDTO dto = postService.updatePost(saved.getId(), request, adminDetails);

        assertEquals("Updated by Admin", dto.getTitle());
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostUnauthorized() {
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        User otherUser = new User();
        otherUser.setUsername("other");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password123");
        otherUser = userRepository.save(otherUser);
        UserDetails otherDetails = UserPrincipal.create(otherUser);

        PostRequest request = new PostRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        assertThrows(RuntimeException.class,
                () -> postService.updatePost(saved.getId(), request, otherDetails));
    }

    @Test
    @SuppressWarnings("null")
    void testUpdatePostClearTags() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setTags(Set.of(tag1, tag2));
        Post saved = postRepository.save(post);

        PostRequest request = new PostRequest();
        request.setTitle("Test Post");
        request.setContent("Test Content");
        request.setTagIds(Collections.emptySet());

        PostDTO dto = postService.updatePost(saved.getId(), request, authorDetails);

        assertTrue(dto.getTags().isEmpty());
    }

    @Test
    @SuppressWarnings("null")
    void testDeletePostByAuthor() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        postService.deletePost(saved.getId(), authorDetails);

        assertFalse(postRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @SuppressWarnings("null")
    void testDeletePostByAdmin() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        postService.deletePost(saved.getId(), adminDetails);

        assertFalse(postRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @SuppressWarnings("null")
    void testDeletePostUnauthorized() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        User otherUser = new User();
        otherUser.setUsername("other");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password123");
        otherUser = userRepository.save(otherUser);
        UserDetails otherDetails = UserPrincipal.create(otherUser);

        assertThrows(RuntimeException.class,
                () -> postService.deletePost(saved.getId(), otherDetails));
    }
}

