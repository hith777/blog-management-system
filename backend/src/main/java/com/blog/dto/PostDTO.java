package com.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorUsername;
    private Long categoryId;
    private String categoryName;
    private Set<TagDTO> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

