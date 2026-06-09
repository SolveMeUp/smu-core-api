package com.solvemeup.smucoreapi.domain.community.service;

import com.solvemeup.smucoreapi.domain.community.cache.PostDetailCache;
import com.solvemeup.smucoreapi.domain.community.dto.request.CommentCreateRequest;
import com.solvemeup.smucoreapi.domain.community.entity.Comment;
import com.solvemeup.smucoreapi.domain.community.entity.Post;
import com.solvemeup.smucoreapi.domain.community.exception.CommentNotFoundException;
import com.solvemeup.smucoreapi.domain.community.repository.CommentReactionRepository;
import com.solvemeup.smucoreapi.domain.community.repository.CommentRepository;
import com.solvemeup.smucoreapi.domain.community.repository.PostRepository;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentReactionRepository commentReactionRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserReader userReader;
    @Mock
    private PostDetailCache postDetailCache;
    @InjectMocks
    private CommentService commentService;

    @Test
    void cannotCreateReplyWithParentFromAnotherPost() {
        Long postId = 1L;
        Long parentId = 10L;
        Post targetPost = org.mockito.Mockito.mock(Post.class);
        Post parentPost = org.mockito.Mockito.mock(Post.class);
        Comment parent = org.mockito.Mockito.mock(Comment.class);
        User user = org.mockito.Mockito.mock(User.class);

        given(postRepository.findByIdAndNotDeleted(postId)).willReturn(Optional.of(targetPost));
        given(userReader.getUser(7L)).willReturn(user);
        given(commentRepository.findByIdAndNotDeleted(parentId)).willReturn(Optional.of(parent));
        given(parent.getPost()).willReturn(parentPost);
        given(parentPost.getId()).willReturn(2L);

        CommentCreateRequest request = new CommentCreateRequest("reply", parentId);

        assertThatThrownBy(() -> commentService.create(7L, postId, request))
                .isInstanceOf(CommentNotFoundException.class);
        verify(commentRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
