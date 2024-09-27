package com.github.aceton41k.config;

import com.github.aceton41k.model.Post;
import com.github.aceton41k.model.Task;
import io.qameta.allure.Step;
import jooq.tables.Comments;
import jooq.tables.Posts;
import jooq.tables.Tasks;
import jooq.tables.records.CommentsRecord;
import jooq.tables.records.PostsRecord;
import jooq.tables.records.TasksRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Table;

import java.util.Objects;
import java.util.Optional;

@Slf4j
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

    @Step("Get task from db")
    public TasksRecord getTask(Long commentId) {
        return Objects.requireNonNull(dsl.selectFrom(Tasks.TASKS)
                .where(Tasks.TASKS.ID.eq(commentId)).fetchOne());
    }

    @Step("Insert task into db")
    public TasksRecord insertTask(Task task) {
        TasksRecord taskRecord = dsl.insertInto(Tasks.TASKS).columns(Tasks.TASKS.STATUS, Tasks.TASKS.PROGRESS)
                .values(task.getStatus(), task.getProgress())
                .returning(Tasks.TASKS.ID, Tasks.TASKS.PROGRESS, Tasks.TASKS.STATUS)
                .fetchOne();

        assert taskRecord != null;
        return taskRecord;

    }

    public void truncateTasks() {
        truncateTable(Tasks.TASKS);
    }

    @Step("Truncate table")
    public void truncateTable(Table<?> table) {
        dsl.truncate(table).execute();
        log.info("Table {} was truncated", table.toString());
    }
}
