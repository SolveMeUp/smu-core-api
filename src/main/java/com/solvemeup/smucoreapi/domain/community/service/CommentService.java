package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.dto.request.CommentCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.entity.Comment;
import com.solvemeup.smucoreapi.domain.community.entity.CommentReaction;
import com.solvemeup.smucoreapi.domain.community.entity.Post;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.community.exception.CommentNotFoundException;
import com.solvemeup.smucoreapi.domain.community.exception.ForbiddenException;
import com.solvemeup.smucoreapi.domain.community.exception.InvalidReplyDepthException;
import com.solvemeup.smucoreapi.domain.community.exception.PostNotFoundException;
import com.solvemeup.smucoreapi.domain.community.repository.CommentReactionRepository;
import com.solvemeup.smucoreapi.domain.community.repository.CommentRepository;
import com.solvemeup.smucoreapi.domain.community.repository.PostRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;
    private final PostRepository postRepository;
    private final UserReader userReader;

    @Transactional
    public CommentResponse create(Long userId, Long postId, CommentCreateRequest request) {
        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(PostNotFoundException::new);

        User user = userReader.getUser(userId);

        Comment comment;

        if (request.parentId() != null) {
            // 대댓글
            Comment parent = commentRepository.findByIdAndNotDeleted(request.parentId())
                    .orElseThrow(CommentNotFoundException::new);

            if (!parent.getPost().getId().equals(postId)) {
                throw new CommentNotFoundException();
            }

            // 1 - depth 제한
            if (parent.isReply()) {
                throw new InvalidReplyDepthException();
            }

            comment = Comment.createReply(post, user, parent, request.content());
        } else {
            // 일반 댓글
            comment = Comment.create(post, user, request.content());
        }

        Comment savedComment = commentRepository.save(comment);
        post.incrementCommentCount();
        return CommentResponse.from(savedComment);
    }

    @Transactional
    public void delete(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdWithPost(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getPost().getId().equals(postId)) {
            throw new CommentNotFoundException();
        }

        if (!comment.isOwner(userId)) {
            throw ForbiddenException.commentDelete();
        }

        Post post = comment.getPost();

        int deletedCount = comment.deleteThread();
        for (int i = 0; i < deletedCount; i++) {
            post.decrementCommentCount();
        }
    }

    @Transactional
    public void react(Long userId, Long commentId, ReactionType reactionType) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User user = userReader.getUser(userId);

        Optional<CommentReaction> existingReaction = commentReactionRepository.findByCommentIdAndUserId(commentId, userId);

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();

            if (reaction.getReactionType() == reactionType) {
                // 같은 반응 다시 클릭하면 취소
                commentReactionRepository.delete(reaction);
                if (reactionType == ReactionType.LIKE) {
                    comment.decrementLikeCount();
                } else {
                    comment.decrementDislikeCount();
                }
            } else {
                // 다른 반응으로 변경
                if (reaction.getReactionType() == ReactionType.LIKE) {
                    comment.decrementLikeCount();
                    comment.incrementDislikeCount();
                } else {
                    comment.decrementDislikeCount();
                    comment.incrementLikeCount();
                }
                reaction.changeReactionType(reactionType);
            }
        } else {
            CommentReaction newReaction = CommentReaction.create(comment, user, reactionType);
            commentReactionRepository.save(newReaction);

            if (reactionType == ReactionType.LIKE) {
                comment.incrementLikeCount();
            } else {
                comment.incrementDislikeCount();
            }
        }
    }
}
