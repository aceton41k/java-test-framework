package com.github.aceton41k.model;

public record Post(
        Integer id,
        String title,
        String message,
        String createdAt,
        String updatedAt
) {

    public Post(String message, String title) {
        this(null, message, title, null, null);
    }
}
