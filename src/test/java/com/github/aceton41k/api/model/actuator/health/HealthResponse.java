package com.github.aceton41k.api.model.actuator.health;

import lombok.Getter;

@Getter
public class HealthResponse {
    public String status;
    public Components components;
}
