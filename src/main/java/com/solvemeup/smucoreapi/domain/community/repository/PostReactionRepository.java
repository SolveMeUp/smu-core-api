package com.solvemeup.smucoreapi.domain.community.repository;

import com.solvemeup.smucoreapi.domain.community.entity.PostReaction;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {

    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);

    boolean existsByPostIdAndUserIdAndReactionType(Long postId, Long userId, ReactionType reactionType);
}
