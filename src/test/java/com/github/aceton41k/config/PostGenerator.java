package com.github.aceton41k.config;

import com.github.aceton41k.api.model.Post;
import net.datafaker.Faker;

public class PostGenerator {
    static Faker faker;
    static {
        faker = new Faker();
    }

    public static Post generate() {
        var title = faker.lorem().sentence();
        var message = faker.lorem().sentence(20);
        return new Post(message, title);
    }
}
