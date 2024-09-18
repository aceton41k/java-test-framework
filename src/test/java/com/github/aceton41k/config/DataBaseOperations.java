package com.github.aceton41k.config;

import com.github.aceton41k.api.model.Post;
import io.qameta.allure.Step;
import jooq.tables.Posts;
import jooq.tables.records.PostsRecord;
import org.jooq.DSLContext;

import java.util.Objects;
import java.util.Optional;

import static org.testng.Assert.assertFalse;

public class DataBaseOperations {
    DSLContext dsl;

    public DataBaseOperations(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Step("Insert post into db")
    public int insertPost(Post post) {
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
    public PostsRecord getPost(int postId) {
        return Objects.requireNonNull(dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne());
    }

    public void assertPostDoesNotExist(int postId, String message) {
        assertFalse(Objects.requireNonNull(dsl.selectCount()
                .from(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne()).value1() != 0, message);
    }
}
