package com.github.aceton41k.api.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

public class UnitTests {
    @Test(description = "Failed test")
    public void failedTest() {
        fail("This test is always failed");
    }

    @Test(description = "Skipped test")
    public void skippedTest() {
        throw new SkipException("This test is always skipped");
    }
}
