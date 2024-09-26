package com.github.aceton41k.unit;

import com.github.aceton41k.config.AvatarGenerator;
import com.github.aceton41k.config.TestDataGenerator;
import com.github.aceton41k.model.UserResponse;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UnitTests {
    @Test(description = "Failed test")
    public void failedTest() {
        fail("This test is always failed");
    }

    @Test(description = "Skipped test")
    public void skippedTest() {
        throw new SkipException("This test is always skipped");
    }

    @Test
    public void postGeneratorTest() {
        var post = TestDataGenerator.generatePost();
        assertFalse(post.title().isEmpty());
        assertFalse(post.message().isEmpty());

    }

    @Test
    public void avatarGeneratorTest() {
        var ag = AvatarGenerator.get();
        assertTrue(ag.length != 0);
    }

     @Test
    public void userResponseConstructorTest() {
        var response = new UserResponse();
    }


}
