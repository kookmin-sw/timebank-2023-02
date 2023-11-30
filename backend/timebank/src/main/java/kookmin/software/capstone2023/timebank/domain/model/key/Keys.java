package kookmin.software.capstone2023.timebank.domain.model.key;

import java.util.Random;

public enum Keys {
    ORIGIN_KEY;

    private final String key;

    private Keys() {
        key = generateRandomString(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    private static String generateRandomString(int length, String source) {
        StringBuilder randomString = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(source.charAt(random.nextInt(source.length())));
        }
        return randomString.toString();
    }

    public String getKey() {
        return key;
    }
}
