package com.github.aceton41k.mobile;

import com.github.aceton41k.mobile.page.LoginPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class AppiumTests extends BaseTestAndroid {

    @Test
    public void androidTest() {

        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isPageLoaded());
        loginPage.login();


        wait.until(webDriver ->
                webDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Posts\")")));

        WebElement recyclerView = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"com.github.aceton41k.simpleapp:id/recycler_view\")"));

        List<WebElement> linearLayouts = recyclerView.findElements(AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.LinearLayout\")"));
        assertEquals(linearLayouts.size(), 6);

        WebElement menu = driver.findElement(AppiumBy.accessibilityId("Open navigation drawer"));
        menu.click();
        wait.until(
                ExpectedConditions.visibilityOf(
                        driver.findElement(
                                AppiumBy.androidUIAutomator("new UiSelector().text(\"Android Studio\")"))));
        driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Android Studio\")"));


        driver.quit();
    }
}
