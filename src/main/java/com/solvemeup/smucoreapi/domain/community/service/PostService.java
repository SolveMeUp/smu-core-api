package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.dto.request.PostCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostUpdateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PageResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostDetailResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostResponse;
import com.solvemeup.smucoreapi.domain.community.entity.Comment;
import com.solvemeup.smucoreapi.domain.community.entity.Post;
import com.solvemeup.smucoreapi.domain.community.entity.PostReaction;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.community.messaging.event.PostIndexEvent;
import com.solvemeup.smucoreapi.domain.community.repository.CommentRepository;
import com.solvemeup.smucoreapi.domain.community.repository.PostReactionRepository;
import com.solvemeup.smucoreapi.domain.community.repository.PostRepository;
import com.solvemeup.smucoreapi.domain.community.exception.*;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PageResponse<PostResponse> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAllActive(pageable);
        Page<PostResponse> postResponses = posts.map(PostResponse::from);
        return PageResponse.from(postResponses);
    }

    @Transactional
    public PostDetailResponse findById(Long postId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        post.incrementViewCount();

        List<CommentResponse> comments = getCommentsWithReplies(postId);

        return PostDetailResponse.of(post, comments);
    }

    @Transactional
    public PostResponse create(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Post post = Post.create(user, request.title(), request.content());
        Post savedPost = postRepository.save(post);

        eventPublisher.publishEvent(PostIndexEvent.index(savedPost));

        return PostResponse.from(savedPost);
    }

    @Transactional
    public PostResponse update(Long userId, Long postId, PostUpdateRequest request) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.isOwner(userId)) {
            throw ForbiddenException.postModify();
        }

        post.update(request.title(), request.content());

        eventPublisher.publishEvent(PostIndexEvent.update(post));

        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.isOwner(userId)) {
            throw ForbiddenException.postDelete();
        }

        post.delete();

        eventPublisher.publishEvent(PostIndexEvent.delete(postId));
    }

    @Transactional
    public void react(Long userId, Long postId, ReactionType reactionType) {
        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Optional<PostReaction> existingReaction = postReactionRepository.findByPostIdAndUserId(postId, userId);

        if (existingReaction.isPresent()) {
            PostReaction reaction = existingReaction.get();
            
            if (reaction.getReactionType() == reactionType) {
                // 같은 반응 다시 클릭하면 취소
                postReactionRepository.delete(reaction);
                if (reactionType == ReactionType.LIKE) {
                    post.decrementLikeCount();
                } else {
                    post.decrementDislikeCount();
                }
            } else {
                // 다른 반응으로 변경
                if (reaction.getReactionType() == ReactionType.LIKE) {
                    post.decrementLikeCount();
                    post.incrementDislikeCount();
                } else {
                    post.decrementDislikeCount();
                    post.incrementLikeCount();
                }
                reaction.changeReactionType(reactionType);
            }
        } else {
            PostReaction newReaction = PostReaction.create(post, user, reactionType);
            postReactionRepository.save(newReaction);
            
            if (reactionType == ReactionType.LIKE) {
                post.incrementLikeCount();
            } else {
                post.incrementDislikeCount();
            }
        }
    }

    private List<CommentResponse> getCommentsWithReplies(Long postId) {
        // 1 - depth 댓글 조회
        List<Comment> rootComments = commentRepository.findRootCommentsByPostId(postId);

        if (rootComments.isEmpty()) {
            return List.of();
        }

        // 대댓글 일괄 조회
        List<Long> rootCommentIds = rootComments.stream()
                .map(Comment::getId)
                .toList();

        List<Comment> allReplies = commentRepository.findRepliesByParentIds(rootCommentIds);

        // parentId 기준으로 그룹핑
        Map<Long, List<CommentResponse>> repliesMap = allReplies.stream()
                .collect(Collectors.groupingBy(
                        reply -> reply.getParent().getId(),
                        Collectors.mapping(CommentResponse::from, Collectors.toList())
                ));

        return rootComments.stream()
                .map(comment -> CommentResponse.of(
                        comment,
                        repliesMap.getOrDefault(comment.getId(), List.of())
                ))
                .toList();
    }
}
