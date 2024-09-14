package com.github.aceton41k.cases;

import com.github.aceton41k.config.PropertyReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Test case to store jira bug
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
    String bugLink = "";

    public TestCase withBugLink(String bugLink) {
        this.bugLink = "BUG: " + PropertyReader.getProperties().getProperty("jira.url") + bugLink + "\n";
        return this;
    }

    public static String bug(String bugLink) {
        return "BUG: " + PropertyReader.getProperties().getProperty("jira.url") + bugLink + "\n";
    }
}
