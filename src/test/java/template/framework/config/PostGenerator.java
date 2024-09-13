package template.framework.config;

import net.datafaker.Faker;
import template.framework.api.model.Post;

public class PostGenerator {
    static Faker faker;
    static {
        faker = new Faker();
    }

    public static Post generate() {
        var title = faker.text().text();
        var message = faker.text().text();
        return new Post()
                .withTitle(title)
                .withMessage(message);
    }
}
