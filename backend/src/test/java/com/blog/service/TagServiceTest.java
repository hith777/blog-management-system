package com.blog.service;

import com.blog.dto.TagDTO;
import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
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
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
    }

    @Test
    void testGetAllTags() {
        Tag tag1 = new Tag();
        tag1.setName("Java");
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("Spring");
        tagRepository.save(tag2);

        List<TagDTO> tags = tagService.getAllTags();

        assertEquals(2, tags.size());
    }

    @Test
    @SuppressWarnings("null")
    void testGetTagById() {
        Tag tag = new Tag();
        tag.setName("Java");
        Tag saved = tagRepository.save(tag);

        TagDTO dto = tagService.getTagById(saved.getId());

        assertEquals("Java", dto.getName());
    }

    @Test
    void testGetTagByIdNotFound() {
        assertThrows(RuntimeException.class, () -> tagService.getTagById(999L));
    }

    @Test
    void testCreateTag() {
        TagRequest request = new TagRequest();
        request.setName("Java");

        TagDTO dto = tagService.createTag(request);

        assertNotNull(dto.getId());
        assertEquals("Java", dto.getName());
    }

    @Test
    void testCreateTagDuplicateName() {
        Tag tag = new Tag();
        tag.setName("Java");
        tagRepository.save(tag);

        TagRequest request = new TagRequest();
        request.setName("Java");

        assertThrows(RuntimeException.class, () -> tagService.createTag(request));
    }

    @Test
    @SuppressWarnings("null")
    void testUpdateTag() {
        Tag tag = new Tag();
        tag.setName("Java");
        Tag saved = tagRepository.save(tag);

        TagRequest request = new TagRequest();
        request.setName("Spring");

        TagDTO dto = tagService.updateTag(saved.getId(), request);

        assertEquals("Spring", dto.getName());
    }

    @Test
    @SuppressWarnings("null")
    void testUpdateTagDuplicateName() {
        Tag tag1 = new Tag();
        tag1.setName("Java");
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName("Spring");
        Tag saved2 = tagRepository.save(tag2);

        TagRequest request = new TagRequest();
        request.setName("Java");

        assertThrows(RuntimeException.class,
                () -> tagService.updateTag(saved2.getId(), request));
    }

    @Test
    @SuppressWarnings("null")
    void testDeleteTag() {
        Tag tag = new Tag();
        tag.setName("Java");
        Tag saved = tagRepository.save(tag);

        tagService.deleteTag(saved.getId());

        assertFalse(tagRepository.findById(saved.getId()).isPresent());
    }
}

