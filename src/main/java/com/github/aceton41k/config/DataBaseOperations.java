package com.github.aceton41k.config;

import com.github.aceton41k.model.Post;
import io.qameta.allure.Step;
import jooq.tables.Comments;
import jooq.tables.Posts;
import jooq.tables.records.CommentsRecord;
import jooq.tables.records.PostsRecord;
import org.jooq.DSLContext;

import java.util.Objects;
import java.util.Optional;

public class DataBaseOperations {
    DSLContext dsl;

    public DataBaseOperations(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Step("Insert post into db")
    public Long insertPost(Post post) {
        dsl.insertInto(Posts.POSTS).columns(Posts.POSTS.TITLE, Posts.POSTS.MESSAGE)
                .values(post.title(), post.message()).execute();

        var optional = Optional.ofNullable(dsl.select(Posts.POSTS.ID)
                .from(Posts.POSTS)
                .where(Posts.POSTS.TITLE.eq(post.title()))
                .fetchOne());

        if (optional.isPresent()) {
            return optional.get().get(Posts.POSTS.ID);
        } else
            throw new RuntimeException("Result of query is empty");
    }

    @Step("Get post from db")
    public PostsRecord getPost(Long postId) {
        return Objects.requireNonNull(dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne());
    }

    @Step("Get comment from db")
    public CommentsRecord getComment(Long commentId) {
        return Objects.requireNonNull(dsl.selectFrom(Comments.COMMENTS)
                .where(Comments.COMMENTS.ID.eq(commentId)).fetchOne());
    }
}
