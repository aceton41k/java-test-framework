package com.github.aceton41k.model;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Long id;
    private Integer progress;
    private String status;
    private String createdAt;
    private String updatedAt;
    private Long createdBy;
}
