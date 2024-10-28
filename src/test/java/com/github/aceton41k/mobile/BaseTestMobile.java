package com.github.aceton41k.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class BaseTestMobile {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() throws URISyntaxException, MalformedURLException {

    }
}
