package template.framework.test;

import com.github.automatedowl.tools.AllureEnvironmentWriter;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import template.framework.test.api.CatFactsApi;
import template.framework.test.api.model.response.CatFactResponse;
import template.framework.test.config.PropertyReader;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MainTests {

    private CatFactsApi catFactsApi;

    @BeforeClass
    void beforeClass() {
        catFactsApi = new CatFactsApi();
    }

    @Test
    public void simpleTest() {
        assertEquals("Hello, world".length(), 12, "length is not valid");
    }

    @Test
    public void catFactsApiTest() {
        catFactsApi.getFactCatBodyResponse()
                .assertStatusCodeOk()
                .assertContainsStringRegExp("cat", "Not contains 'cat'");
        CatFactResponse response = catFactsApi.getCatFactResponse();
        assertNotNull(response.getFact());
        assertEquals(response.getFact().length(), response.getLength(), "length is not valid");

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
