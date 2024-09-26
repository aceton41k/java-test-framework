package com.github.aceton41k.model;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
}
