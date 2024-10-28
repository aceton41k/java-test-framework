package com.github.aceton41k.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class BaseTestAndroid  extends  BaseTestMobile {

    @BeforeClass
    public void setUp() throws URISyntaxException, MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options
                .setAppPackage("com.github.aceton41k.simpleapp")
                .setAppActivity(".activity.LoginActivity")
                .setPlatformVersion("12")
                .setDeviceName("emulator-5554")
                .setPlatformName("Android")
                .noReset();

        driver = new AndroidDriver(new URI("http://192.168.0.200:4723").toURL(), options);
        driver.activateApp("com.github.aceton41k.simpleapp");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 секунд ожидания
    }
}
