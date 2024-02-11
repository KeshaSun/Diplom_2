package edu.practicum.utils;

import java.util.Random;

public class Utils {
    static Random random = new Random();
    public static String randomString(int length){

        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit));
            buffer.append(Character.toChars(randomLimitedInt));
        }
        return buffer.toString();
    }
}