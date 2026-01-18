package com.solvemeup.smucoreapi.domain.community.controller;

import com.solvemeup.smucoreapi.domain.community.dto.request.CommentCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostCreateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.request.PostUpdateRequest;
import com.solvemeup.smucoreapi.domain.community.dto.response.CommentResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PageResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostDetailResponse;
import com.solvemeup.smucoreapi.domain.community.dto.response.PostResponse;
import com.solvemeup.smucoreapi.domain.community.enums.ReactionType;
import com.solvemeup.smucoreapi.domain.community.service.CommentService;
import com.solvemeup.smucoreapi.domain.community.service.PostService;
import com.solvemeup.smucoreapi.global.oauth2.annotation.AuthUser;
import com.solvemeup.smucoreapi.global.oauth2.session.UserSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    @GetMapping
    public ResponseEntity<PageResponse<PostResponse>> getAllPosts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthUser UserSession userSession,
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.create(userSession.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthUser UserSession userSession,
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.update(userSession.id(), postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthUser UserSession userSession,
            @PathVariable Long postId
    ) {
        postService.delete(userSession.id(), postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(
            @AuthUser UserSession userSession,
            @PathVariable Long postId
    ) {
        postService.react(userSession.id(), postId, ReactionType.LIKE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/dislikes")
    public ResponseEntity<Void> dislikePost(
            @AuthUser UserSession userSession,
            @PathVariable Long postId
    ) {
        postService.react(userSession.id(), postId, ReactionType.DISLIKE);
        return ResponseEntity.ok().build();
    }
}
