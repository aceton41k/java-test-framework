package com.github.aceton41k.api.model.actuator.health;

import lombok.Getter;

@Getter
public class Details{
    public String database;
    public String validationQuery;
    public long total;
    public long free;
    public int threshold;
    public String path;
    public boolean exists;
}