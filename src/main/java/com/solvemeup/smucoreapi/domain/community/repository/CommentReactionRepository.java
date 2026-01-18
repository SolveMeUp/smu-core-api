package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.entity.CommentReaction;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {

    Optional<CommentReaction> findByCommentIdAndUserId(Long commentId, Long userId);

    boolean existsByCommentIdAndUserIdAndReactionType(Long commentId, Long userId, ReactionType reactionType);
}
