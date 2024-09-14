package com.github.aceton41k.api.client;

import com.github.aceton41k.api.model.Post;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostApiClient extends BaseApiClient {


    @Step("Get post")
    public Response getPost(int id) {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/posts/{id}", id);
    }

    @Step("Create post")
    public Response createPost(Post post) {
        return given().spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .post("/api/posts");
    }

    @Step("Delete post")
    public Response deletePost(int id) {
        return given().spec(reqSpec)
                .delete("/api/posts/{id}", id);
    }

    @Step("Update post")
    public Response updatePost(int id, Post post) {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .body(post)
                .put("/api/posts/{id}", id);

    }

    @Step("Get method with exception")
    public Response exception() {
        return given()
                .spec(reqSpec)
                .contentType(ContentType.JSON)
                .get("/api/ex");
    }

}

