package com.github.aceton41k.api;

import com.github.aceton41k.client.ActuatorApiClient;
import com.github.aceton41k.config.DataBaseOperations;
import com.github.aceton41k.config.PropertyReader;
import com.github.aceton41k.model.actuator.ActuatorInfoResponse;
import com.github.aceton41k.model.git.Build;
import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class BaseApiTest extends ApiAsserts {
    static DSLContext dsl = null;
    private static Connection connection;

    protected static DataBaseOperations dbo;
    protected static DataBaseAsserts dba;
    Faker faker = new Faker();

    @BeforeSuite
    public void beforeSuite() {

        String dbUrl = Objects.requireNonNull(System.getProperty("db.url"));
        String dbUser = PropertyReader.getDbUser();
        String dbPassword = PropertyReader.getDbPassword();

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            dsl = DSL.using(connection);

        } catch (SQLException e) {
            throw new RuntimeException("Could not create db connection", e.getCause());
        }

        dbo = new DataBaseOperations(dsl);
        dba = new DataBaseAsserts(dsl);
    }

    @AfterSuite
    public void afterSuite() {

        dbo.truncateTasks();

        closeConnection();

        ActuatorApiClient actuatorApi = new ActuatorApiClient();
        ActuatorInfoResponse response = actuatorApi.getInfo();
        Optional<Build> buildOptional = Optional.ofNullable(response.getGit().getBuild()); // используем ofNullable, чтобы избежать NullPointerException
        String version = buildOptional.map(Build::getVersion).orElse("none");

        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Build branch", "%s (tag: %s)".formatted(
                                response.getGit().getBranch(),
                                version))
                        .put("Commit No", response.getGit().getCommit().getId())
                        .put("Stand url", PropertyReader.getBaseUrl())
                        .build(),
                System.getProperty("user.dir") + "/build/allure-results/"
        );
    }

    @Step("Close jdbc connection")
    public void closeConnection() {
        try {
            connection.close();
            log.info("DB Connection closed");
        } catch (SQLException e) {
            throw new RuntimeException("Could not close jdbc connection successfully", e.getCause());
        }
    }


}
