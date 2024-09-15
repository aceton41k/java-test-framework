package com.github.aceton41k.api.test;

import com.github.aceton41k.api.ApiAsserts;
import com.github.aceton41k.config.PropertyReader;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Step;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class BaseApiTest extends ApiAsserts {
    static DSLContext dsl = null;
    private static Connection connection;

    @BeforeClass
    public void setUp() {
        String dbUrl = Objects.requireNonNull(System.getProperty("db.url"));
        String dbUser = PropertyReader.getDbUser();
        String dbPassword = PropertyReader.getDbPassword();

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            dsl = DSL.using(connection);

        } catch (SQLException e) {
            throw new RuntimeException("Could not create db connection", e.getCause());
        }
    }

    @AfterClass
    public void afterClass() {

        closeConnection();

        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Backend version", "10.5.6.7")
                        .put("Stand", PropertyReader.getBaseUrl())
                        .build(),
                System.getProperty("user.dir") + "/build/allure-results/"
        );
    }

    @Step("Close jdbc connection")
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not close jdbc connection successfully", e.getCause());
        }
    }


}
