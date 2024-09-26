package com.github.aceton41k.api;

import jooq.tables.Comments;
import jooq.tables.Posts;
import org.jooq.DSLContext;

import java.util.Objects;

import static org.testng.Assert.assertFalse;

public class DataBaseAsserts {
    DSLContext dsl;
    public DataBaseAsserts(DSLContext dsl) {
        this.dsl = dsl;
    }
    public void assertPostNotExist(Long postId, String message) {
        assertFalse(Objects.requireNonNull(dsl.selectCount()
                .from(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne()).value1() != 0, message);
    }

    public void assertCommentNotExist(Long commentId) {
        assertFalse(Objects.requireNonNull(dsl.selectCount()
                .from(Comments.COMMENTS)
                .where(Comments.COMMENTS.ID.eq(commentId)).fetchOne()).value1() != 0);
    }
}
