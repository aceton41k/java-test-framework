package com.github.aceton41k.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения учетных записей для нагрузочных тестов
 */

@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    @Getter
    List<Credentials> credentials = new ArrayList<>();

    @AllArgsConstructor
    @Getter
    public static class Credentials {
        String login;
        String password;
        Boolean admin;
        Boolean busy;
    }

    public void addCredentials(String login, String password, boolean admin) {
        this.credentials.add(new Credentials(login, password, admin, false));
    }
}
