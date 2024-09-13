package template.framework.api.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import template.framework.api.client.AuthApiClient;
import template.framework.api.client.PostApiClient;
import template.framework.api.model.Post;
import template.framework.config.DataBaseOperations;
import template.framework.config.PostGenerator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class AppTests extends BaseApiTest {

    PostApiClient postApi;
    DataBaseOperations dbo;


    @BeforeClass
    public void beforeClass() {
        dbo = new DataBaseOperations(dsl);
        postApi = new PostApiClient();
        AuthApiClient authApi = new AuthApiClient();
        var email = authApi.signUpAny();
        authApi.auth(email, "123456");
    }


    @Test(description = "Get post")
    public void getPostTest() {
        Post post = PostGenerator.generate();
        int insertedPostId = dbo.insertPost(post);
        Post response = postApi.getPost(insertedPostId);
        assertEquals(response.getId(), insertedPostId, "id from response isn't equal to db's");
        assertEquals(response.getMessage(), post.getMessage(), "message from response isn't equal to db's");
        assertEquals(response.getTitle(), post.getTitle(), "title from response isn't equal to db's");
    }

    @Test(description = "Create post")
    public void createPost() {
        var post = PostGenerator.generate();
        Post response = postApi.createPost(post);
        assertNotNull(response.getId());
        assertEquals(response.getMessage(), post.getMessage(), "message from response isn't equal to generated");
        assertEquals(response.getTitle(), post.getTitle());
        int createdPostId = response.getId();

        var postRecord = dbo.getPost(createdPostId);
        assertEquals(response.getId(), postRecord.getId(), "id from response isn't equal to db's");
        assertEquals(post.getTitle(), postRecord.getTitle(), "title from response isn't equal to db's");
        assertEquals(post.getMessage(), postRecord.getMessage(), "message from response isn't equal to db's");
    }

    @Test(description = "Update post")
    public void updatePost() {
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var updatedPost = PostGenerator.generate();
        var response = postApi.updatePost(insertedPostId, updatedPost);
        assertEquals(response.getMessage(), updatedPost.getMessage(), "message from response is not equal to updated");
        assertEquals(response.getTitle(), updatedPost.getTitle(), "title from response is not equal to updated");

        var postRecord = dbo.getPost(insertedPostId);
        assertEquals(response.getId(), postRecord.getId(), "id from response is not equal to db's");
        assertEquals(response.getTitle(), postRecord.getTitle(), "title from response is not equal to db's");
        assertEquals(response.getMessage(), postRecord.getMessage(), "message from response is not equal to db's");
    }

    @Test(description = "Delete post")
    public void deletePost() {
        int insertedPostId = dbo.insertPost(PostGenerator.generate());
        var response = postApi.deletePost(insertedPostId);
        assertEquals(response.statusCode(), 204);

        //todo check post deleted from db
    }

}
