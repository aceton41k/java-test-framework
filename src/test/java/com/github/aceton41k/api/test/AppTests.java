package com.github.aceton41k.api.test;

import com.github.aceton41k.api.client.ActuatorApiClient;
import com.github.aceton41k.api.client.AuthApiClient;
import com.github.aceton41k.api.client.PostApiClient;
import com.github.aceton41k.api.client.UserApiClient;
import com.github.aceton41k.api.model.Post;
import com.github.aceton41k.api.model.UserResponse;
import com.github.aceton41k.api.model.actuator.health.HealthResponse;
import com.github.aceton41k.api.model.page.Page;
import com.github.aceton41k.config.DataBaseOperations;
import com.github.aceton41k.config.PostGenerator;
import io.qameta.allure.Severity;
import io.qameta.allure.testng.Tag;
import io.restassured.common.mapper.TypeRef;
import net.datafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.qameta.allure.SeverityLevel.*;
import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.GET;
import static org.testng.Assert.*;


public class AppTests extends BaseApiTest {

    PostApiClient postApi;
    UserApiClient userApi;
    DataBaseOperations dbo;
    Faker faker = new Faker();
    String email = faker.internet().emailAddress();
    String fullName = faker.name().fullName();
    String password = faker.internet().password(5, 5);


    @BeforeClass
    public void beforeClass() {


        dbo = new DataBaseOperations(dsl);
        postApi = new PostApiClient();
        userApi = new UserApiClient();
        AuthApiClient authApi = new AuthApiClient();
        authApi.signUp(email, password, fullName);
        authApi.auth(email, password);
    }

    @Test(description = "Get post")
    @Severity(BLOCKER)
    public void getPost() {
        Post post = PostGenerator.generate();
        int insertedPostId = dbo.insertPost(post);
        var response = postApi.getPost(insertedPostId);
        var postResponse = response.as(Post.class);
        assertStatusCodeOk(response);

        assertEquals(postResponse.id(), insertedPostId, "id from response isn't equal to db's");
        assertEquals(postResponse.message(), post.message(), "message from response isn't equal to db's");
        assertEquals(postResponse.title(), post.title(), "title from response isn't equal to db's");
    }

    @Test(description = "Create post")
    @Severity(BLOCKER)
    public void createPost() {
        var post = PostGenerator.generate();
        var response = postApi.createPost(post);
        assertStatusCreated(response);
        var postResponse = response.as(Post.class);

        assertNotNull(postResponse.id());
        assertEquals(postResponse.message(), post.message(), "message from response isn't equal to generated");
        assertEquals(postResponse.title(), post.title(), "title from response isn't equal to generated");

        int createdPostId = postResponse.id();
        var postFromDb = dbo.getPost(createdPostId);

        assertEquals(createdPostId, postFromDb.getId(), "id from response isn't equal to db's");
        assertEquals(post.title(), postFromDb.getTitle(), "title from response isn't equal to db's");
        assertEquals(post.message(), postFromDb.getMessage(), "message from response isn't equal to db's");
    }

    @Test(description = "Update post")
    @Severity(BLOCKER)
    public void updatePost() {
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var updatedPost = PostGenerator.generate();
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
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var response = postApi.deletePost(insertedPostId);
        assertStatusNoContent(response);
        dbo.assertPostDoesNotExist(insertedPostId, "post that should be deleted exists in db");
    }

    @Test(description = "Delete post not exists", groups = {"negative"})
    @Severity(MINOR)
    public void deletePostNotExist() {
        var response = postApi.deletePost(999999);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Get posts")
    @Severity(BLOCKER)
    public void getPosts() {
        var post1 = PostGenerator.generate();
        var post2 = PostGenerator.generate();
        dbo.insertPost(post1);
        dbo.insertPost(post2);

        var response = postApi.getPosts();

        assertStatusCodeOk(response);
        Page<Post> posts = response.as(new TypeRef<>() {
        });
        assertTrue(posts.getContent().size() >= 2);

    }

    @Test(description = "Get post not exists", groups = {"negative"})
    @Severity(MINOR)
    public void getPostNotExist() {
        var response = postApi.getPost(999999);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Update post not exists", groups = {"negative"})
    @Severity(MINOR)
    public void updatePostNotExist() {
        var post = PostGenerator.generate();
        var response = postApi.updatePost(999999, post);
        assertStatusCodeNotFound(response);
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
        assertStatusCodeOk(response);
        var healthResponse = response.as(HealthResponse.class);
        assertEquals(healthResponse.getStatus(), "UP", "status");

    }

}
