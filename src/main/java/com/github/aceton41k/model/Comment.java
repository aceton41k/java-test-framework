package com.github.aceton41k.model;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Long id;
    private String message;
    private String createdAt;
    private String updatedAt;
    private Long createdBy;
    private Long modifiedBy;
}
