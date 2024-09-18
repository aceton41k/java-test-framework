package com.github.aceton41k.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс для хранения логина и пароля. Используется в многопоточных тестах
 */

@NoArgsConstructor
@Setter
@Getter
public class EmailRegUserCredentials {
    private String login;
    private String password;
}
