package com.github.aceton41k.api.model.actuator.health;

import lombok.Getter;

@Getter
public class Components {
    public Db db;
    public DiskSpace diskSpace;
    public Ping ping;
}
