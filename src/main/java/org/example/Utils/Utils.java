package org.example.Utils;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    public static int getRandomNumber(int min, int max) {
        return random.nextInt(max - min) + min;
    }
    public static String getRandomDeliveryDate(){
        long today = new Date().getTime();
        long end = new Date(today + TimeUnit.DAYS.toMillis(1)*365).getTime();
        long diff = end - today + 1;
        long rand = today + (long)(Math.random() * diff);
        Timestamp randTime = new Timestamp(rand);
        return randTime.toString();
    }
}