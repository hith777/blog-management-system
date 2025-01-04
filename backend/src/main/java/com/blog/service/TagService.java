package com.blog.service;

import com.blog.dto.TagDTO;
import com.blog.dto.TagRequest;
import com.blog.entity.Tag;
import com.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagDTO getTagById(@NonNull Long id) {
        // orElseThrow guarantees non-null, but type system doesn't know this
        Tag tag = Objects.requireNonNull(tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id)),
                "Tag must not be null after orElseThrow");
        return convertToDTO(tag);
    }

    @Transactional
    public TagDTO createTag(TagRequest tagRequest) {
        if (tagRepository.existsByName(tagRequest.getName())) {
            throw new RuntimeException("Tag with this name already exists");
        }

        Tag tag = new Tag();
        tag.setName(tagRequest.getName());

        Tag savedTag = tagRepository.save(tag);
        return convertToDTO(savedTag);
    }

    @Transactional
    public TagDTO updateTag(@NonNull Long id, TagRequest tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!tag.getName().equals(tagRequest.getName()) &&
            tagRepository.existsByName(tagRequest.getName())) {
            throw new RuntimeException("Tag with this name already exists");
        }

        tag.setName(tagRequest.getName());

        Tag updatedTag = tagRepository.save(tag);
        return convertToDTO(updatedTag);
    }

    @Transactional
    public void deleteTag(@NonNull Long id) {
        // orElseThrow guarantees non-null, but type system doesn't know this
        Tag tag = Objects.requireNonNull(tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id)),
                "Tag must not be null after orElseThrow");

        tagRepository.delete(tag);
    }

    private TagDTO convertToDTO(@NonNull Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setCreatedAt(tag.getCreatedAt());
        dto.setUpdatedAt(tag.getUpdatedAt());
        return dto;
    }
}

