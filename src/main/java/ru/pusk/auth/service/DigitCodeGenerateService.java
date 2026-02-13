package ru.pusk.auth.service;

import java.util.Random;

public class DigitCodeGenerateService {
    public static String generateCodeDigits(int numberOfDigits) {
        Random random = new Random();
        StringBuilder numbers = new StringBuilder();

        for (int i = 0; i < numberOfDigits; i++) {
            int number = random.nextInt(10);
            numbers.append(number);
        }
        return numbers.toString();
    }
}
