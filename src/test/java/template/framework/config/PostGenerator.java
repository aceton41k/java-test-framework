package template.framework.config;

import net.datafaker.Faker;
import template.framework.api.model.Post;

import java.util.concurrent.TimeUnit;

public class PostGenerator {
    static Faker faker;
    static {
        faker = new Faker();
    }

    public static Post generate() {
        var title = faker.text().text();
        var message = faker.text().text();
        String date = faker.timeAndDate().past(10, TimeUnit.DAYS).toString();
        return new Post()
                .withDate(date)
                .withTitle(title)
                .withMessage(message);
    }
}
