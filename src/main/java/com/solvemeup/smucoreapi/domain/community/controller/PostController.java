package com.solvemeup.smucoreapi.domain.community.controller;

import com.solvemeup.smucoreapi.domain.community.dto.request.CommentCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostUpdateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.CursorResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostDetailResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostSearchResponse;
import com.solvemeup.smucoreapi.domain.community.service.PostSearchService;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.community.service.CommentService;
import com.solvemeup.smucoreapi.domain.community.service.PostService;
import com.solvemeup.smucoreapi.global.dto.response.PageResponse;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUserId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostSearchService postSearchService;
    private final CommentService commentService;


    @GetMapping
    public ResponseEntity<CursorResponse<PostResponse>> getAllPosts(
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return ResponseEntity.ok(postService.findAllByCursor(lastId, size));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<PostSearchResponse>> searchPosts(
            @RequestParam @NotBlank String keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(postSearchService.search(keyword, pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        PostDetailResponse response = postService.findById(postId);
        postService.incrementView(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthUserId Long userId,
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthUserId Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.update(userId, postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthUserId Long userId,
            @PathVariable Long postId
    ) {
        postService.delete(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(
            @AuthUserId Long userId,
            @PathVariable Long postId
    ) {
        postService.react(userId, postId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/dislikes")
    public ResponseEntity<Void> dislikePost(
            @AuthUserId Long userId,
            @PathVariable Long postId
    ) {
        postService.react(userId, postId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthUserId Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.create(userId, postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthUserId Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(userId, postId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<Void> likeComment(
            @AuthUserId Long userId,
            @PathVariable Long commentId
    ) {
        commentService.react(userId, commentId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/dislikes")
    public ResponseEntity<Void> dislikeComment(
            @AuthUserId Long userId,
            @PathVariable Long commentId
    ) {
        commentService.react(userId, commentId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }
}
