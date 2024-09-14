package com.github.aceton41k.config;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Клас перезапуска упавших тестов
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    int retryCount = 0; // кол-во попыток

    @Override
    public boolean retry(ITestResult result) {

        int maxRetryCount = 5;
        if (retryCount < maxRetryCount) {
            retryCount++;
            result.getThrowable().printStackTrace();
            System.out.println("Попытка №" + retryCount + " для теста - " + result.getMethod().getDescription());
            return true;
        }
        return false;
    }
}
