package com.github.aceton41k.config;

import io.restassured.http.Cookies;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Класс для для эмуляции работы одного пользователя в нагрузочных тестах
 */
@Slf4j
public class UserThread implements Runnable {
    String login;
    String password;
    Cookies cookies;
    int userId;


    @Override
    public void run() {
        Thread.currentThread().setName(login);
        log.info("user {} started", login);
        try {

            // USER ACTIONS
            waitRandom(10, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("user {} ended", login);
    }

    public UserThread(String login, String password) {
        this.login = login;
        this.password = password;

    }

    // Задержка между действиями пользователя
    void waitRandom(int minSec, int maxSec) {
        Random rnd = new Random();
        try {
            Thread.sleep((rnd.nextInt(maxSec) + minSec) * 1000L);
        } catch (InterruptedException ignored) {
        }
    }

}
