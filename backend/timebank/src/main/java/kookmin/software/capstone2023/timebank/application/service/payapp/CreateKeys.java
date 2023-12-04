package kookmin.software.capstone2023.timebank.application.service.payapp;

import kookmin.software.capstone2023.timebank.domain.model.key.Keys;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateKeys {
    private static final String originalKey = Keys.ORIGIN_KEY.getKey();

    public String createKey(){
        return generateSHA256Hash();
    }
    // SHA-256 해시 함수를 이용하여 키 생성
    private String generateSHA256Hash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(originalKey.getBytes(StandardCharsets.UTF_8));

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 생성된 해시된 키를 검증하는 메서드

}
