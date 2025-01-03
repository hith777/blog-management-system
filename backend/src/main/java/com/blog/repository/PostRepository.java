package com.blog.repository;

import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);
    List<Post> findByCategory(Category category);
    List<Post> findByAuthorId(Long authorId);
    List<Post> findByCategoryId(Long categoryId);
    
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.id = :tagId")
    List<Post> findByTagId(@Param("tagId") Long tagId);
    
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.author LEFT JOIN FETCH p.category")
    List<Post> findAllWithRelations();
    
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.author LEFT JOIN FETCH p.category WHERE p.id = :id")
    java.util.Optional<Post> findByIdWithRelations(@Param("id") Long id);
}

