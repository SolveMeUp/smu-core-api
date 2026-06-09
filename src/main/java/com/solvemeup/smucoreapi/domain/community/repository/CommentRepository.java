package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.post.id = :postId AND c.parent IS NULL AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRootCommentsByPostId(Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.parent.id IN :parentIds AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParentIds(List<Long> parentIds);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.deletedAt IS NULL AND c.post.deletedAt IS NULL")
    Optional<Comment> findByIdAndNotDeleted(Long id);

    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.id = :id AND c.deletedAt IS NULL AND c.post.deletedAt IS NULL")
    Optional<Comment> findByIdWithPost(Long id);
}
