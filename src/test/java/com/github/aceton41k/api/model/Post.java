package com.github.aceton41k.api.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
public class Post extends JsonPrintable {
    private Integer id;
    private String title;
    private String message;
    private String createdAt;
    private String updatedAt;
}
