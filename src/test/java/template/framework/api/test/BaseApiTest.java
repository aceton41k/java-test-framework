package template.framework.api.test;

import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import template.framework.config.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseApiTest {
    DSLContext dsl = null;

    @BeforeClass
    public void setUp() {
        String url = "jdbc:postgresql://localhost:5432/appdb";
        String user = "appuser";
        String password = "appuser123$";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            dsl = DSL.using(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public void afterClass() {
        AllureEnvironmentWriter.allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Версия бекенда", "10.5.6.7")
                        .put("Стенд", PropertyReader.getBaseUrl())
                        .build(),
                System.getProperty("user.dir") + "/build/allure-results/"
        );
    }
}
