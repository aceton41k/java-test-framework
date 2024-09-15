package com.github.aceton41k.api.test;

import com.github.aceton41k.api.client.AuthApiClient;
import com.github.aceton41k.api.client.PostApiClient;
import com.github.aceton41k.api.client.UserApiClient;
import com.github.aceton41k.api.model.Post;
import com.github.aceton41k.api.model.UserResponse;
import com.github.aceton41k.config.DataBaseOperations;
import com.github.aceton41k.config.PostGenerator;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import net.datafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class AppTests extends BaseApiTest {

    PostApiClient postApi;
    UserApiClient userApi;
    DataBaseOperations dbo;
    Faker faker = new Faker();
    String email = faker.internet().emailAddress();
    String fullName = faker.name().fullName();
    String password = faker.internet().password(5 ,5);


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
    @Severity(SeverityLevel.BLOCKER)
    public void getPost() {
        Post post = PostGenerator.generate();
        int insertedPostId = dbo.insertPost(post);
        var response = postApi.getPost(insertedPostId);
        var postResponse = response.as(Post.class);
        assertStatusCodeOk(response);

        assertEquals(postResponse.getId(), insertedPostId, "id from response isn't equal to db's");
        assertEquals(postResponse.getMessage(), post.getMessage(), "message from response isn't equal to db's");
        assertEquals(postResponse.getTitle(), post.getTitle(), "title from response isn't equal to db's");
    }

    @Test(description = "Create post")
    @Severity(SeverityLevel.BLOCKER)
    public void createPost() {
        var post = PostGenerator.generate();
        var response = postApi.createPost(post);
        assertStatusCodeOk(response);
        var postResponse = response.as(Post.class);

        assertNotNull(postResponse.getId());
        assertEquals(postResponse.getMessage(), post.getMessage(), "message from response isn't equal to generated");
        assertEquals(postResponse.getTitle(), post.getTitle(), "title from response isn't equal to generated");

        int createdPostId = postResponse.getId();
        var postFromDb = dbo.getPost(createdPostId);

        assertEquals(createdPostId, postFromDb.getId(), "id from response isn't equal to db's");
        assertEquals(post.getTitle(), postFromDb.getTitle(), "title from response isn't equal to db's");
        assertEquals(post.getMessage(), postFromDb.getMessage(), "message from response isn't equal to db's");
    }

    @Test(description = "Update post")
    @Severity(SeverityLevel.BLOCKER)
    public void updatePost() {
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var updatedPost = PostGenerator.generate();
        var response = postApi.updatePost(insertedPostId, updatedPost);
        assertStatusCodeOk(response);
        var postResponse = response.as(Post.class);
        assertEquals(postResponse.getMessage(), updatedPost.getMessage(), "message from response is not equal to updated");
        assertEquals(postResponse.getTitle(), updatedPost.getTitle(), "title from response is not equal to updated");

        var postRecord = dbo.getPost(insertedPostId);
        assertEquals(postResponse.getId(), postRecord.getId(), "id from response is not equal to db's");
        assertEquals(updatedPost.getTitle(), postRecord.getTitle(), "title from response is not equal to db's");
        assertEquals(updatedPost.getMessage(), postRecord.getMessage(), "message from response is not equal to db's");
    }

    @Test(description = "Delete post")
    @Severity(SeverityLevel.BLOCKER)
    public void deletePost() {
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var response = postApi.deletePost(insertedPostId);
        assertStatusNoContent(response);
        dbo.assertPostDoesNotExist(insertedPostId, "post that should be deleted exists in db");
    }

    @Test(description = "Delete post not exists", groups = {"negative"})
    @Severity(SeverityLevel.MINOR)
    public void deletePostNotExist() {
        var response = postApi.deletePost(999999);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Get post not exists", groups = {"negative"})
    @Severity(SeverityLevel.MINOR)
    public void getPostNotExist() {
        var response = postApi.getPost(999999);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Update post not exists", groups = {"negative"})
    @Severity(SeverityLevel.MINOR)
    public void updatePostNotExist() {
        var post = PostGenerator.generate();
        var response = postApi.updatePost(999999, post);
        assertStatusCodeNotFound(response);
    }

    @Test(description = "Get method that throws exception", groups = {"negative"})
    @Severity(SeverityLevel.TRIVIAL)
    public void getExceptionRequest() {
        var response = postApi.exception();
        assertInternalServerError(response);
    }

    @Test(description = "User info")
    @Severity(SeverityLevel.NORMAL)
    public void userInfo() {
        var response = userApi.me();
        assertStatusCodeOk(response);
        var userResponse = response.as(UserResponse.class);
        assertEquals(userResponse.getEmail(), email);
        assertEquals(userResponse.getFullName(), fullName);
        assertStatusCodeOk(response);
    }

    //todo realize dedicated api client for unauthorized access
//    @Test(description = "Unauthorized access to get post")
//    @Severity(SeverityLevel.NORMAL)
//    public void unauthorizedAccess() {
//        var postApi = new PostApiClient();
//        var response = postApi.getPost(1);
//        assertStatusCodeUnauthorized(response);
//    }

}
