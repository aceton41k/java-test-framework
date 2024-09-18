package com.github.aceton41k.model;

import lombok.Getter;

import java.util.List;

@Getter
public class UserResponse {

    private Integer id;
    private String fullName;
    private String email;
    private String createdAt;
    private String updatedAt;
    private Object password;
    private List<Object> authorities;
    private String username;
    private String enabled;
    private Boolean credentialsNonExpired;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
}
