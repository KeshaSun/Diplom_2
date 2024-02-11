package org.example.user;

import com.github.javafaker.Faker;
import org.example.models.User;
import static org.example.Utils.Utils.randomString;

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