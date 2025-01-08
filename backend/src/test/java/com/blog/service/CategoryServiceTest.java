package com.blog.service;

import com.blog.dto.CategoryDTO;
import com.blog.dto.CategoryRequest;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.repository.CategoryRepository;
import com.blog.repository.PostRepository;
import com.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetAllCategories() {
        Category cat1 = new Category();
        cat1.setName("Tech");
        cat1.setDescription("Technology");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setName("Science");
        cat2.setDescription("Science");
        categoryRepository.save(cat2);

        List<CategoryDTO> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology");
        Category saved = categoryRepository.save(category);

        CategoryDTO dto = categoryService.getCategoryById(saved.getId());

        assertEquals("Tech", dto.getName());
        assertEquals("Technology", dto.getDescription());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(999L));
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Tech");
        request.setDescription("Technology");

        CategoryDTO dto = categoryService.createCategory(request);

        assertNotNull(dto.getId());
        assertEquals("Tech", dto.getName());
        assertEquals("Technology", dto.getDescription());
    }

    @Test
    void testCreateCategoryDuplicateName() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology");
        categoryRepository.save(category);

        CategoryRequest request = new CategoryRequest();
        request.setName("Tech");
        request.setDescription("Different");

        assertThrows(RuntimeException.class, () -> categoryService.createCategory(request));
    }

    @Test
    void testUpdateCategory() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology");
        Category saved = categoryRepository.save(category);

        CategoryRequest request = new CategoryRequest();
        request.setName("Technology");
        request.setDescription("Updated Description");

        CategoryDTO dto = categoryService.updateCategory(saved.getId(), request);

        assertEquals("Technology", dto.getName());
        assertEquals("Updated Description", dto.getDescription());
    }

    @Test
    void testUpdateCategoryDuplicateName() {
        Category cat1 = new Category();
        cat1.setName("Tech");
        cat1.setDescription("Technology");
        categoryRepository.save(cat1);

        Category cat2 = new Category();
        cat2.setName("Science");
        cat2.setDescription("Science");
        Category saved2 = categoryRepository.save(cat2);

        CategoryRequest request = new CategoryRequest();
        request.setName("Tech");
        request.setDescription("Different");

        assertThrows(RuntimeException.class,
                () -> categoryService.updateCategory(saved2.getId(), request));
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology");
        Category saved = categoryRepository.save(category);

        categoryService.deleteCategory(saved.getId());

        assertFalse(categoryRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void testDeleteCategoryWithPosts() {
        User author = new User();
        author.setUsername("author");
        author.setEmail("author@example.com");
        author.setPassword("password123");
        author = userRepository.save(author);

        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology");
        Category saved = categoryRepository.save(category);

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setAuthor(author);
        post.setCategory(saved);
        postRepository.save(post);

        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(saved.getId()));
    }
}

