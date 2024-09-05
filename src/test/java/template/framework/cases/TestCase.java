package template.framework.cases;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import template.framework.config.PropertyReader;

/**
 * Класс реализующий тест-кейс с указанием ссылки на дефект
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
    String bugLink = "";

    public TestCase withBugLink(String bugLink) {
        this.bugLink = "BUG: " + PropertyReader.getJiraIssueUrl() + bugLink + "\n";
        return this;
    }

    /**
     * Для печати ссылки на баг в стектрейсе
     */
    public static String bug(String bugLink) {
        return "BUG: " + PropertyReader.getJiraIssueUrl() + bugLink + "\n";
    }
}
