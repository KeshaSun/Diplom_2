package edu.practicum.models;
import lombok.*;

@Data
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

    public User() {

    }
}