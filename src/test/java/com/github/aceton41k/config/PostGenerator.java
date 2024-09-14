package com.github.aceton41k.config;

import com.github.aceton41k.api.model.Post;
import net.datafaker.Faker;

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
