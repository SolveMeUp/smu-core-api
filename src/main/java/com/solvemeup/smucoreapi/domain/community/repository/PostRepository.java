package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id < :lastId AND p.deletedAt IS NULL ORDER BY p.id DESC")
    List<Post> findAllByCursor(@Param("lastId") Long lastId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.deletedAt IS NULL ORDER BY p.id DESC")
    List<Post> findAllFirstPage(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Post> findByIdAndNotDeleted(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Post> findByIdWithUser(Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id IN :ids AND p.deletedAt IS NULL")
    List<Post> findAllByIdsWithUser(@Param("ids") List<Long> ids);

    @Query(value = "SELECT p FROM Post p JOIN FETCH p.user",
           countQuery = "SELECT count(p) FROM Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + :delta WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId, @Param("delta") Long delta);
}
