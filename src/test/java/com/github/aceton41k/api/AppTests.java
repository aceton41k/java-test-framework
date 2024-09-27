package com.github.aceton41k.api;


import com.github.aceton41k.RetryAnalyzer;
import com.github.aceton41k.client.ActuatorApiClient;
import com.github.aceton41k.client.AuthApiClient;
import com.github.aceton41k.client.PostApiClient;
import com.github.aceton41k.client.UserApiClient;
import com.github.aceton41k.config.DataBaseOperations;
import com.github.aceton41k.config.TestDataGenerator;
import com.github.aceton41k.model.Comment;
import com.github.aceton41k.model.ErrorResponse;
import com.github.aceton41k.model.Post;
import com.github.aceton41k.model.UserResponse;
import com.github.aceton41k.model.page.Page;
import io.qameta.allure.Severity;
import io.qameta.allure.testng.Tag;
import io.restassured.common.mapper.TypeRef;
import net.datafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Instant;

import static io.qameta.allure.SeverityLevel.*;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.GET;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.testng.Assert.*;


public class AppTests extends BaseApiTest {

    PostApiClient postApi;
    UserApiClient userApi;
    DataBaseOperations dbo;
    DataBaseAsserts dba;
    Faker faker = new Faker();
    String email = faker.internet().emailAddress();
    String fullName = faker.name().fullName();
    String password = faker.internet().password(5, 5);


    @BeforeClass
    public void beforeClass() {

        dbo = new DataBaseOperations(dsl);
        dba = new DataBaseAsserts(dsl);
        postApi = new PostApiClient();
        userApi = new UserApiClient();
        AuthApiClient authApi = new AuthApiClient();
        authApi.signUp(email, password, fullName);
        authApi.auth(email, password);
    }

    @Test(description = "Get post")
    @Severity(BLOCKER)
    public void getPost() {
        Post post = TestDataGenerator.generatePost();
        long insertedPostId = dbo.insertPost(post);
        var response = postApi.getPost(insertedPostId);
        var postResponse = response.as(Post.class);
        assertStatusCodeOk(response);

        assertEquals(postResponse.id(), insertedPostId, "id from response isn't equal to db's");
        assertEquals(postResponse.message(), post.message(), "message from response isn't equal to db's");
        assertEquals(postResponse.title(), post.title(), "title from response isn't equal to db's");
    }

    @Test(description = "Create post",
            retryAnalyzer = RetryAnalyzer.class)
    @Severity(BLOCKER)
    public void createPost() {
        Instant createdDate = Instant.now();
        var post = TestDataGenerator.generatePost();
        var response = postApi.createPost(post);
        assertStatusCreated(response);
        var postResponse = response.as(Post.class);
        Long createdPostId = postResponse.id();

        assertNotNull(postResponse.id());
        assertEquals(postResponse.message(), post.message(), "message from response isn't equal to generated");
        assertEquals(postResponse.title(), post.title(), "title from response isn't equal to generated");


        var postFromDb = dbo.getPost(createdPostId);
        Instant createdDateResponse = Instant.parse(postResponse.createdAt());

        assertDatesEquals(createdDate, createdDateResponse, 1, SECONDS);
        assertEquals(createdPostId, postFromDb.getId(), "id from response isn't equal to db's");
        assertEquals(post.title(), postFromDb.getTitle(), "title from response isn't equal to db's");
        assertEquals(post.message(), postFromDb.getMessage(), "message from response isn't equal to db's");
    }

    @Test(description = "Update post")
    @Severity(BLOCKER)
    public void updatePost() {
        long insertedPostId = dbo.insertPost(TestDataGenerator.generatePost());
        var updatedPost = TestDataGenerator.generatePost();
        var response = postApi.updatePost(insertedPostId, updatedPost);
        assertStatusCodeOk(response);
        var postResponse = response.as(Post.class);
        assertEquals(postResponse.message(), updatedPost.message(), "message from response is not equal to updated");
        assertEquals(postResponse.title(), updatedPost.title(), "title from response is not equal to updated");

        var postRecord = dbo.getPost(insertedPostId);
        assertEquals(postResponse.id(), postRecord.getId(), "id from response is not equal to db's");
        assertEquals(updatedPost.title(), postRecord.getTitle(), "title from response is not equal to db's");
        assertEquals(updatedPost.message(), postRecord.getMessage(), "message from response is not equal to db's");
    }

    @Test(description = "Delete post")
    @Severity(BLOCKER)
    public void deletePost() {
        long insertedPostId = dbo.insertPost(TestDataGenerator.generatePost());
        var response = postApi.deletePost(insertedPostId);
        assertStatusNoContent(response);
        dba.assertPostNotExist(insertedPostId, "post that should be deleted exists in db");
    }

    @Test(description = "Delete post not exists", groups = {"negative"})
    @Severity(MINOR)
    public void deletePostNotExist() {
        var response = postApi.deletePost(999999L);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Get posts")
    @Severity(BLOCKER)
    public void getPosts() {
        var post1 = TestDataGenerator.generatePost();
        var post2 = TestDataGenerator.generatePost();
        dbo.insertPost(post1);
        dbo.insertPost(post2);

        var response = postApi.getPosts();

        assertStatusCodeOk(response);
        Page<Post> posts = response.as(new TypeRef<>() {
        });
        assertTrue(posts.getContent().size() >= 2);

    }

    @Test(description = "Get posts with paging params")
    @Severity(BLOCKER)
    public void getPostsParams() {
        var post1 = TestDataGenerator.generatePost();
        var post2 = TestDataGenerator.generatePost();
        dbo.insertPost(post1);
        dbo.insertPost(post2);
        int size = 2;
        int page = 3;
        var response = postApi.getPosts(size, page);

        assertStatusCodeOk(response);
        Page<Post> postsResponse = response.as(new TypeRef<>() {
        });
        assertEquals(postsResponse.getSize(), Integer.valueOf(size));
        assertEquals(postsResponse.getNumber(), Integer.valueOf(page));
    }

    @Test(description = "Get posts with empty paging params",
            dataProvider = "pageParamsDataProvider",
            groups = {"negative"})
    @Severity(BLOCKER)
    public void getPostsPageParams(Integer size, Integer page) {
        var response = postApi.getPosts(size, page);
        assertStatusCodeOk(response);
    }

    @Test
    public void getPostsInvalidParamsPage() {
        var response = postApi.getPosts(0, -1);
        assertInternalServerError(response);
        ErrorResponse error =  response.as(ErrorResponse.class);
        assertEquals(error.getDetail(), "Page index must not be less than zero");
    }

    @Test
    public void getPostsInvalidParamsSize() {
        var response = postApi.getPosts(0, 1);
        assertInternalServerError(response);
        ErrorResponse error =  response.as(ErrorResponse.class);
        assertEquals(error.getDetail(), "Page size must not be less than one");
    }

    @Test
    public void getPostsInvalidParamsPage2() {
        var response = postApi.getPosts(5, -1);
        assertInternalServerError(response);
        ErrorResponse error =  response.as(ErrorResponse.class);
        assertEquals(error.getDetail(), "Page index must not be less than zero");
    }



    @DataProvider(name = "pageParamsDataProvider")
    public Object[][] pageParamsDataProvider() {
        return new Object[][]{{null, null}, {2, null}, {null, 1}, {1, 2}};
    }

    @Test(description = "Get post not exists",
            groups = {"negative"})
    @Severity(MINOR)
    public void getPostNotExist() {
        var response = postApi.getPost(999999L);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Update post not exists", groups = {"negative"})
    @Severity(MINOR)
    public void updatePostNotExist() {
        var post = TestDataGenerator.generatePost();
        var response = postApi.updatePost(999999L, post);
        assertStatusCodeNotFound(response);
    }

    @Test
    public void addComment() {
        var post = TestDataGenerator.generatePost();
        var postResponse = postApi.createPost(post).as(Post.class);
        var postId = postResponse.id();
        var generatedComment = TestDataGenerator.generateComment();
        var response = postApi.addComment(postId, generatedComment);
        Instant commentCreatedDateTest = Instant.now();
        assertStatusCreated(response);
        Comment commentResponse = response.as(Comment.class);

        Instant commentCreatedDateResponse = Instant.parse(commentResponse.getCreatedAt());
        assertDatesEquals(commentCreatedDateTest, commentCreatedDateResponse, 1, SECONDS);

        var commentFromDb = dbo.getComment(commentResponse.getId());
        assertEquals(commentResponse.getId(), commentFromDb.getId());
        assertEquals(commentResponse.getMessage(), commentFromDb.getMessage());
    }

    @Test
    public void updateComment() {
        var post = TestDataGenerator.generatePost();
        var postId = postApi.createPost(post).as(Post.class).id();
        var comment = TestDataGenerator.generateComment();
        var commentId = postApi.addComment(postId, comment)
                .as(Comment.class).getId();

        String editedMessage = "Edited message";
        var editedComment = new Comment().withMessage(editedMessage);
        var response = postApi.updateComment(postId, commentId, editedComment);

        Instant commentUpdatedDateTest = Instant.now();
        assertStatusCodeOk(response);
        Comment commentResponse = response.as(Comment.class);

        assertEquals(commentResponse.getMessage(), editedMessage, "message from response and edited are not equal");

        Instant commentUpdatedDateResponse = Instant.parse(commentResponse.getUpdatedAt());
        assertDatesEquals(commentUpdatedDateTest, commentUpdatedDateResponse, 1, SECONDS);
    }

    @Test
    public void deleteComment() {
        var post = TestDataGenerator.generatePost();
        var postId = postApi.createPost(post).as(Post.class).id();
        var comment = TestDataGenerator.generateComment();
        var commentId = postApi.addComment(postId, comment)
                .as(Comment.class).getId();

        var response = postApi.deleteComment(postId, commentId);

        assertStatusNoContent(response);

        dba.assertCommentNotExist(commentId);

        var getCommentResponse = postApi.getComment(postId, commentId);
        assertStatusCodeNotFound(getCommentResponse);
    }

    @Test
    public void deleteCommentNotExist() {
        long commentIdNotExist = 999999L;
        var post = TestDataGenerator.generatePost();
        var postId = postApi.createPost(post).as(Post.class).id();
        var response = postApi.deleteComment(postId, commentIdNotExist);
        assertStatusCodeNotFound(response);
        var error = response.as(ErrorResponse.class);
        assertEquals(error.getDetail(), "Comment with ID %d was not found".formatted(commentIdNotExist));
        assertEquals(error.getInstance(), "/api/posts/%s/comments/%s".formatted(postId, commentIdNotExist));
    }

    @Test
    public void deleteCommentPostNotExist() {
        long postIdNotExist = 999999L;
        var response = postApi.deleteComment(postIdNotExist, 999999L);
        assertStatusCodeNotFound(response);
        var error = response.as(ErrorResponse.class);
        assertEquals(error.getDetail(), "Post with ID %d was not found".formatted(postIdNotExist));
    }

    @Test(description = "Get method that throws exception", groups = {"negative"})
    @Severity(TRIVIAL)
    public void getExceptionRequest() {
        var response = postApi.exception();
        assertInternalServerError(response);
    }

    @Test(description = "User info")
    @Severity(NORMAL)
    public void userInfo() {
        var response = userApi.me();
        assertStatusCodeOk(response);
        var userResponse = response.as(UserResponse.class);
        assertEquals(userResponse.getEmail(), email);
        assertEquals(userResponse.getFullName(), fullName);
        assertStatusCodeOk(response);
    }

    @Test(description = "Unauthorized access to get post",
            groups = {"security"})
    @Severity(NORMAL)
    public void getPostUnauthorized() {
        var response = postApi.anonymousRequest("/api/posts", GET);
        assertStatusCodeUnauthorized(response);
    }

    @Test(description = "Unauthorized access to delete post",
            groups = {"negative"})
    @Severity(CRITICAL)
    @Tag("negative")
    public void deletePostUnauthorized() {
        var response = postApi.anonymousRequest("/api/posts", DELETE);
        assertStatusCodeUnauthorized(response);
    }

    @Test(description = "Actuator health")
    @Severity(CRITICAL)
    public void getHealth() {
        var actuatorApi = new ActuatorApiClient();
        var response = actuatorApi.getHealth();
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.getStatus(), "UP", "status");
        soft.assertEquals(response.getComponents().getPing().getStatus(), "UP", "ping");
        soft.assertTrue(response.getComponents().getDiskSpace().getDetails().getFree() > 0L, "free");
        soft.assertTrue(response.getComponents().getDiskSpace().getDetails().getThreshold() > 0, "threshold");
        soft.assertNotNull(response.getComponents().getDiskSpace().getDetails().getPath(), "path");
        soft.assertTrue(response.getComponents().getDiskSpace().getDetails().getTotal() > 0L, "total");
        soft.assertNotNull(response.getComponents().getDb().getDetails().getDatabase(), "database");
        soft.assertNotNull(response.getComponents().getDiskSpace().getStatus(), "disk space status");
        soft.assertAll();
    }

}
