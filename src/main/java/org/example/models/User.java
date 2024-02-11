package org.example.models;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {

    String email;
    String password;
    String name;

    public User(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }
}