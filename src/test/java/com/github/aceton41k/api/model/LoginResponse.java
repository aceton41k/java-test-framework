package com.github.aceton41k.api.model;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private Integer expiresIn;
}
