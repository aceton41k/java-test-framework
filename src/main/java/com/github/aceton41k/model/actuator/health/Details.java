package com.github.aceton41k.model.actuator.health;

import lombok.Getter;

@Getter
public class Details{
    private String database;
    private String validationQuery;
    private long total;
    private long free;
    private int threshold;
    private String path;
    private boolean exists;
}