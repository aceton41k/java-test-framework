package com.github.aceton41k.api.model;

import com.google.gson.Gson;

public class JsonPrintable {
    private final static Gson gson = new Gson();

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
