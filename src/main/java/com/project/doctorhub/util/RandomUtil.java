package com.project.doctorhub.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {

    public String generateRandomNumber(int length) {
        Random random = new Random();
        long domain = (int) (1 * Math.pow(10, length - 1));
        long bound = (int) (8 * Math.pow(10, length - 1));

        return Long.toString(random.nextInt((int) bound) + domain);
    }


}
