package com.blog.repository;

import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.Tag;
import com.blog.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

    private User author;
    private Category category;
    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setUsername("author");
        author.setEmail("author@example.com");
        author.setPassword("password123");
        author = userRepository.save(author);

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
    }

    @Test
    void testSavePost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);

        Post saved = postRepository.save(post);

        assertNotNull(saved.getId());
        assertEquals("Test Post", saved.getTitle());
        assertEquals("Test Content", saved.getContent());
        assertEquals(author.getId(), saved.getAuthor().getId());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void testSavePostWithCategory() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setCategory(category);

        Post saved = postRepository.save(post);

        assertNotNull(saved.getCategory());
        assertEquals(category.getId(), saved.getCategory().getId());
    }

    @Test
    void testSavePostWithTags() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setTags(Set.of(tag1, tag2));

        Post saved = postRepository.save(post);

        assertEquals(2, saved.getTags().size());
        assertTrue(saved.getTags().contains(tag1));
        assertTrue(saved.getTags().contains(tag2));
    }

    @Test
    void testFindAllWithRelations() {
        Post post1 = new Post();
        post1.setTitle("Post 1");
        post1.setContent("Content 1");
        post1.setAuthor(author);
        post1.setCategory(category);
        post1.setTags(Set.of(tag1));
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Post 2");
        post2.setContent("Content 2");
        post2.setAuthor(author);
        post2.setTags(Set.of(tag2));
        postRepository.save(post2);

        List<Post> posts = postRepository.findAllWithRelations();

        assertEquals(2, posts.size());
        Post found = posts.stream()
                .filter(p -> p.getTitle().equals("Post 1"))
                .findFirst()
                .orElseThrow();
        assertNotNull(found.getAuthor());
        assertNotNull(found.getCategory());
        assertEquals(1, found.getTags().size());
    }

    @Test
    void testFindByIdWithRelations() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setCategory(category);
        post.setTags(Set.of(tag1, tag2));
        Post saved = postRepository.save(post);

        Optional<Post> found = postRepository.findByIdWithRelations(saved.getId());

        assertTrue(found.isPresent());
        Post foundPost = found.get();
        assertNotNull(foundPost.getAuthor());
        assertNotNull(foundPost.getCategory());
        assertEquals(2, foundPost.getTags().size());
    }

    @Test
    void testDeletePost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        Post saved = postRepository.save(post);

        postRepository.delete(saved);

        Optional<Post> found = postRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}

