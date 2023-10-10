package template.framework.test.ui;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;

public class BaseTest {
    @BeforeClass
    public static void setUp() {
        // Установка настроек Selenide
        Configuration.browser = Browsers.CHROME;
        Configuration.baseUrl = "https://mail.ru/";
        Configuration.timeout = 10000;

    }
}
