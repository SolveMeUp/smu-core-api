package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.dto.request.PostCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostUpdateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.CursorResponse;
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
import com.solvemeup.smucoreapi.domain.community.exception.ForbiddenException;
import com.solvemeup.smucoreapi.domain.community.exception.PostNotFoundException;
import com.solvemeup.smucoreapi.domain.community.viewcount.ViewCountStorage;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.global.cache.CacheConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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
    private final UserReader userReader;
    private final ApplicationEventPublisher eventPublisher;
    private final ViewCountStorage viewCountStorage;

    public CursorResponse<PostResponse> findAllByCursor(Long lastId, int size) {
        Pageable pageable = Pageable.ofSize(size + 1);

        List<Post> posts = (lastId == null)
                ? postRepository.findAllFirstPage(pageable)
                : postRepository.findAllByCursor(lastId, pageable);

        boolean hasNext = posts.size() > size;
        List<Post> content = hasNext ? posts.subList(0, size) : posts;

        List<PostResponse> responses = content.stream()
                .map(PostResponse::from)
                .toList();

        Long nextLastId = content.isEmpty() ? null : content.get(content.size() - 1).getId();

        return CursorResponse.of(responses, size, hasNext, nextLastId);
    }

    @Cacheable(value = CacheConfig.POST_DETAIL, key = "#postId")
    public PostDetailResponse findById(Long postId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostNotFoundException::new);

        List<CommentResponse> comments = getCommentsWithReplies(postId);

        return PostDetailResponse.of(post, comments);
    }

    public void incrementView(Long postId) {
        viewCountStorage.increment(postId);
    }

    @Transactional
    public PostResponse create(Long userId, PostCreateRequest request) {
        User user = userReader.getUser(userId);

        Post post = Post.create(user, request.title(), request.content());
        Post savedPost = postRepository.save(post);

        eventPublisher.publishEvent(PostIndexEvent.index(savedPost));

        return PostResponse.from(savedPost);
    }

    @CacheEvict(value = CacheConfig.POST_DETAIL, key = "#postId")
    @Transactional
    public PostResponse update(Long userId, Long postId, PostUpdateRequest request) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.isOwner(userId)) {
            throw ForbiddenException.postModify();
        }

        post.update(request.title(), request.content());

        eventPublisher.publishEvent(PostIndexEvent.update(post));

        return PostResponse.from(post);
    }

    @CacheEvict(value = CacheConfig.POST_DETAIL, key = "#postId")
    @Transactional
    public void delete(Long userId, Long postId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!post.isOwner(userId)) {
            throw ForbiddenException.postDelete();
        }

        post.delete();

        eventPublisher.publishEvent(PostIndexEvent.delete(postId));
    }

    @CacheEvict(value = CacheConfig.POST_DETAIL, key = "#postId")
    @Transactional
    public void react(Long userId, Long postId, ReactionType reactionType) {
        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(PostNotFoundException::new);

        User user = userReader.getUser(userId);

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
