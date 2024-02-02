package com.sudol.SudolAPI;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        String adminPassword = "admin";
        String nauczyciel1Password = "nauczyciel1";
        String nauczyciel2Password = "nauczyciel2";

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String adminPasswordEncoded = passwordEncoder.encode(adminPassword);
        String nauczyciel1PasswordEncoded = passwordEncoder.encode(nauczyciel1Password);
        String nauczyciel2PasswordEncoded = passwordEncoder.encode(nauczyciel2Password);

        System.out.println(adminPassword);
        System.out.println(adminPasswordEncoded);
        System.out.println(nauczyciel1Password);
        System.out.println(nauczyciel1PasswordEncoded);
        System.out.println(nauczyciel2Password);
        System.out.println(nauczyciel2PasswordEncoded);

    }
}
