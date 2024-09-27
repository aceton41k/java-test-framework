package com.github.aceton41k.client;

import com.github.aceton41k.model.Comment;
import com.github.aceton41k.model.Post;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostApiClient extends BaseApiClient {


    @Step("Get post")
    public Response getPost(Long id) {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/{id}", id);
    }

    @Step("Get posts")
    public Response getPosts() {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts");
    }

    @Step("Get posts")
    public Response getPosts(Integer size, Integer page) {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .queryParam("size", size)
                .queryParam("page", page)
                .get("/api/posts");
    }

    @Step("Create post")
    public Response createPost(Post post) {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .post("/api/posts");
    }

    @Step("Delete post")
    public Response deletePost(Long id) {
        return given().spec(reqSpec)
                .delete("/api/posts/{id}", id);
    }

    @Step("Update post")
    public Response updatePost(Long id, Post post) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .put("/api/posts/{id}", id);

    }

    @Step("Add comment to post")
    public Response addComment(Long postId, Comment comment) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(comment)
                .post("/api/posts/{id}/comments", postId);
    }

    @Step("Get comment")
    public Response getComment(Long postId, Long commentId) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/{postIdd}/comments/{commentId}", postId, commentId);
    }

    @Step("Get all comments of post")
    public Response getComments(Long postId) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/{postIdd}/comments", postId);
    }


    @Step("Update comment")
    public Response updateComment(Long postId, Long commentId, Comment comment) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(comment)
                .put("/api/posts/{postId}/comments/{commentId}", postId, commentId);
    }

    @Step("Delete comment")
    public Response deleteComment(Long postId, Long commentId) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .delete("/api/posts/{postId}/comments/{commentId}", postId, commentId);
    }



    @Step("Get method with exception")
    public Response exception() {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/ex");
    }

    @Step("Create task")
    public Response createTask(Integer duration) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .queryParam("duration", duration)
                .post("/api/tasks");
    }

    @Step("Get task")
    public Response getTask(Long id) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/tasks/{id}", id);
    }

    @Step("Get task")
    public Response getAllTasks() {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/tasks");
    }
}

