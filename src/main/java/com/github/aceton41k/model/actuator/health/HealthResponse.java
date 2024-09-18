package com.github.aceton41k.model.actuator.health;

import lombok.Getter;

@Getter
public class HealthResponse {
    private String status;
    private Components components;
}
