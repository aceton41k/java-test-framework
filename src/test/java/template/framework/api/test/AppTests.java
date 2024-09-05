package template.framework.api.test;

import jooq.tables.Posts;
import org.testng.annotations.*;
import template.framework.api.client.PostApiClient;
import template.framework.config.PostGenerator;
import template.framework.api.model.Post;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


public class AppTests extends BaseApiTest {

    PostApiClient api;
    int insertedPostId;
    Post post;


    @BeforeClass
    public void beforeClass() {
        api = new PostApiClient();

        post = PostGenerator.generate();
        dsl.insertInto(Posts.POSTS).columns(Posts.POSTS.TITLE, Posts.POSTS.MESSAGE, Posts.POSTS.DATE)
                .values(post.getTitle(), post.getMessage(), post.getDate()).execute();

        insertedPostId = dsl.select(Posts.POSTS.ID).from(Posts.POSTS).where(Posts.POSTS.TITLE.eq(post.getTitle())).fetchOne().get(Posts.POSTS.ID);
    }



    @Test(description = "Get post")
    public void getPostTest() {

        Post response = api.getPost(insertedPostId);
        assertEquals(response.getId(), insertedPostId);
        assertEquals(response.getMessage(), post.getMessage());
        assertEquals(response.getTitle(), post.getTitle());
    }

    @Test(description = "Create post")
    public void createPost() {
        var post = PostGenerator.generate();
        Post response = api.createPost(post);
        assertNotNull(response.getId());
        assertEquals(response.getMessage(), post.getMessage());
        assertEquals(response.getTitle(), post.getTitle());
        assertEquals(response.getDate(), post.getDate());
        int createdPostId = response.getId();

        var createdPost = api.getPost(createdPostId);
        assertEquals(createdPost.getMessage(), post.getMessage());
        assertEquals(createdPost.getTitle(), post.getTitle());
        assertEquals(createdPost.getDate(), post.getDate());

        var messageFromDb = dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.MESSAGE.eq(post.getMessage())).fetchOne().getValue(Posts.POSTS.MESSAGE);
        assertEquals(post.getMessage(), messageFromDb);
    }

    @Test(description = "Update post")
    public void updatePost() {
        var updatedPost = PostGenerator.generate();
        var response = api.updatePost(insertedPostId, updatedPost);
        assertEquals(response.getMessage(), updatedPost.getMessage());
        assertEquals(response.getTitle(), updatedPost.getTitle());
        assertEquals(response.getDate(), updatedPost.getDate());

        var updatedMessageFromDb = dsl.selectFrom(Posts.POSTS)
                .where(Posts.POSTS.ID.eq(insertedPostId)).fetchOne().getValue(Posts.POSTS.MESSAGE);
        assertEquals(updatedPost.getMessage(), updatedMessageFromDb);
    }

    @Test(description = "Delete post")
    public void deletePost() {
        var response = api.deletePost(insertedPostId);
        assertEquals(response.statusCode(), 204);
    }

    @AfterClass
    public void afterClass() {
    }
}
