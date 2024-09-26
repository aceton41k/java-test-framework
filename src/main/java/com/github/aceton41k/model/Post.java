package com.github.aceton41k.model;

public record Post(
        Long id,
        String title,
        String message,
        String createdAt,
        String updatedAt,
        String createdBy,
        String modifiedBy

) {

    public Post(String message, String title) {
        this(null, message, title, null, null, null, null);
    }
}
