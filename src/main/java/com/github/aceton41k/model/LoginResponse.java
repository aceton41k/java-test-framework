package com.github.aceton41k.model;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private Integer expiresIn;
}
