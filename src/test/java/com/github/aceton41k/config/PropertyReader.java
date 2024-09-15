package com.github.aceton41k.config;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyReader {
    @Getter
    private static final Properties properties;
    @Getter
    private static final String baseUrl;
    @Getter
    public static final String dbUrl;
    @Getter
    private static final String dbUser;
    @Getter
    private static final String dbPassword;

    static {
        properties = new Properties();
        try (InputStream inputStream = new FileInputStream("src/test/resources/test.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load file test.properties", e.getCause());
        }
        baseUrl = System.getProperty("api.url", properties.getProperty("api.url", "http://default:8080"));

        dbUrl = System.getProperty("db.url", properties.getProperty("db.url"));
        dbUser = System.getProperty("db.user", properties.getProperty("db.user"));
        dbPassword = System.getProperty("db.password", properties.getProperty("db.password"));

    }


    private PropertyReader() {
    }
}