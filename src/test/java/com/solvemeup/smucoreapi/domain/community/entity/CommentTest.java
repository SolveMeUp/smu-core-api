package com.solvemeup.smucoreapi.domain.community.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void deleteThreadSoftDeletesRootAndActiveReplies() {
        Comment root = Comment.create(null, null, "root");
        Comment firstReply = Comment.createReply(null, null, root, "first");
        Comment secondReply = Comment.createReply(null, null, root, "second");
        secondReply.delete();

        int deletedCount = root.deleteThread();

        assertThat(deletedCount).isEqualTo(2);
        assertThat(root.isDeleted()).isTrue();
        assertThat(firstReply.isDeleted()).isTrue();
        assertThat(secondReply.isDeleted()).isTrue();
    }
}
