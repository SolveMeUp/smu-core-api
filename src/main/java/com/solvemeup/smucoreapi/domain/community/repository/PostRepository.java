package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    Page<Post> findAllActive(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Post> findByIdAndNotDeleted(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Post> findByIdWithUser(Long id);
}
