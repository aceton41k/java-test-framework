package com.github.aceton41k.mobile.page;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    @FindBy(xpath = "//android.widget.EditText[@resource-id=\"email_input\"]")
    WebElement email;

    @FindBy(xpath = "//android.widget.EditText[@resource-id=\"password_input\"]")
    WebElement password;

    @FindBy(xpath = "//android.view.View[@resource-id=\"login_button\"]")
    WebElement loginButton;

    public LoginPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public void login() {
        email.sendKeys("a@a.a");
        password.sendKeys("123456");
        loginButton.click();
    }

    public boolean isPageLoaded() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOf(
                            email));

            wait.until(
                    ExpectedConditions.visibilityOf(
                            password));

            wait.until(
                    ExpectedConditions.visibilityOf(
                            loginButton));
        } catch (TimeoutException _) {

        }
        return true;
    }

}
