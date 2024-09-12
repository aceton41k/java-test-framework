package template.framework.config;

import jooq.tables.Posts;
import jooq.tables.records.PostsRecord;
import org.jooq.DSLContext;
import template.framework.api.model.Post;

import java.util.Objects;

public class DataBaseOperations {
    DSLContext dsl;

    public DataBaseOperations(DSLContext dsl) {
        this.dsl = dsl;
    }

    public int insertPost(Post post) {
        dsl.insertInto(Posts.POSTS).columns(Posts.POSTS.TITLE, Posts.POSTS.MESSAGE)
                .values(post.getTitle(), post.getMessage()).execute();

        return dsl.select(Posts.POSTS.ID).from(Posts.POSTS).where(Posts.POSTS.TITLE.eq(post.getTitle())).fetchOne().get(Posts.POSTS.ID);
    }

    public String getPostMessage(int postId) {
        return Objects.requireNonNull(dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne()).getValue(Posts.POSTS.MESSAGE);
    }

    public PostsRecord getPost(int postId) {
        return Objects.requireNonNull(dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(postId)).fetchOne());
    }
}
