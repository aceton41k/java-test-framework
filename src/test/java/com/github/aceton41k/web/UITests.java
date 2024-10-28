package com.github.aceton41k.web;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

public class UITests extends BaseTest {

    //@Test
    public void fluentWaitTest() {
        open("");
//        $x("//button[text()='Найти']").getText();
        FluentWait<WebDriver> fluentWait = new FluentWait<>(WebDriverRunner.getWebDriver())
                .withTimeout(Duration.ofSeconds(10)) // Максимальное время ожидания
                .pollingEvery(Duration.ofSeconds(1)) // Частота проверки
                .ignoring(NoSuchElementException.class, TimeoutException.class); // Исключения, которые нужно игнорировать

        WebElement input = fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.name("text")));
// Действие с элементом после успешного ожидания
    }

}
