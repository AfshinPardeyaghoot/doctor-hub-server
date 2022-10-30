package com.project.doctorhub.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StringUtil {

    public String generateRandomNumber(int length) {
        Random random = new Random();

        if (length < 1)
            throw new IllegalArgumentException("طول عدد باید بزرگ تر از صفر باشد");

        long domain = (int) (1 * Math.pow(10, length - 1));
        long bound = (int) (8 * Math.pow(10, length - 1));

        return Long.toString(random.nextInt((int) bound) + domain);
    }
}
