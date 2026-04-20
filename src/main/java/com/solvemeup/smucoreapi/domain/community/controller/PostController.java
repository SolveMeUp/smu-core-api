package com.solvemeup.smucoreapi.domain.community.controller;

import com.solvemeup.smucoreapi.domain.auth.oauth2.principal.CustomOAuth2User;
import com.solvemeup.smucoreapi.domain.community.dto.request.CommentCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostUpdateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.CursorResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PageResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostDetailResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostSearchResponse;
import com.solvemeup.smucoreapi.domain.community.service.PostSearchService;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.community.service.CommentService;
import com.solvemeup.smucoreapi.domain.community.service.PostService;
import com.solvemeup.smucoreapi.global.security.annotation.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(postService.findAllByCursor(lastId, size));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<PostSearchResponse>> searchPosts(
            @RequestParam String keyword,
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
            @AuthUser CustomOAuth2User user,
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.create(user.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.update(user.getUserId(), postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId
    ) {
        postService.delete(user.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId
    ) {
        postService.react(user.getUserId(), postId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/dislikes")
    public ResponseEntity<Void> dislikePost(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId
    ) {
        postService.react(user.getUserId(), postId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }

    /*
    댓글 api 일단 여기로
     */

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.create(user.getUserId(), postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        commentService.delete(user.getUserId(), postId, commentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<Void> likeComment(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long commentId
    ) {
        commentService.react(user.getUserId(), commentId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/dislikes")
    public ResponseEntity<Void> dislikeComment(
            @AuthUser CustomOAuth2User user,
            @PathVariable Long commentId
    ) {
        commentService.react(user.getUserId(), commentId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }
}
