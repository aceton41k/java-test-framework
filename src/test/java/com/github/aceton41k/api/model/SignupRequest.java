package com.github.aceton41k.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
}
