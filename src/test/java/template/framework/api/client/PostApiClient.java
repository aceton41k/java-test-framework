package template.framework.api.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import template.framework.api.model.Post;

import static io.restassured.RestAssured.given;

public class PostApiClient extends AbstractApiClient {


    public Post getPost(int id) {
        httpResponse = given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/{id}", id);
        return httpResponse.as(Post.class);

    }

    public Post createPost(Post post) {
        httpResponse = given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .post("/api/posts");
        return httpResponse.as(Post.class);
    }

    public Response deletePost(int id) {
        return given().spec(reqSpec)
                .delete("/api/posts/{id}", id);
    }

    public Post updatePost(int id, Post post) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .put("/api/posts/{id}", id)
                .as(Post.class);
    }
}

