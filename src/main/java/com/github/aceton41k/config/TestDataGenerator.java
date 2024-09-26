package com.github.aceton41k.config;

import com.github.aceton41k.model.Comment;
import com.github.aceton41k.model.Post;
import net.datafaker.Faker;

public class TestDataGenerator {
    static Faker faker;

    static {
        faker = new Faker();
    }

    public static Post generatePost() {
        var title = faker.lorem().sentence();
        var message = faker.lorem().sentence(20);
        return new Post(message, title);
    }

    public static Comment generateComment() {
        var message = faker.lorem().sentence(20);
        return new Comment().withMessage(message);
    }
}
