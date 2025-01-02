package com.ra.genpass;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class TestingGenpass {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);

        System.out.println("Password matches: " + encoder.matches(rawPassword, encodedPassword));
    }
}
