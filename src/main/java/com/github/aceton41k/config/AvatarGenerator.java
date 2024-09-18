package com.github.aceton41k.config;

import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.cat.CatAvatar;

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
}
