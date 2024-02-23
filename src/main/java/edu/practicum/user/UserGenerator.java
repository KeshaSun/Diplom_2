package edu.practicum.user;

import com.github.javafaker.Faker;
import edu.practicum.models.User;
import static edu.practicum.utils.Utils.randomString;

public class UserGenerator {

    static Faker faker = new Faker();

    public static User randomUser(){
        return new User().builder()
                .email(faker.internet().emailAddress())
                .password(faker.bothify("56???????"))
                .name(randomString(5))
                .build();
    }
}