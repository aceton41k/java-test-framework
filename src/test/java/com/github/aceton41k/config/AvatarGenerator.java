package com.github.aceton41k.config;

import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.cat.CatAvatar;

import java.io.File;
import java.util.Random;

/**
 * Генерация аватаров для пользователей
 */
public class AvatarGenerator {
    public static byte[] get() {
        Avatar avatar = CatAvatar.newAvatarBuilder()
                .size(100, 100)
                .build();
        return avatar.createAsPngBytes(new Random().nextLong());
    }

    public static void get2() {
        Avatar avatar = CatAvatar.newAvatarBuilder()
                .size(500, 500)
                .build();
        var rnd = new Random().nextLong();
        avatar.createAsPngToFile(rnd, new File("src/main/resources/img/av" + rnd+".png"));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            get2();
        }

    }
}
