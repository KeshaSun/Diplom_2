package org.example.models;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserCred {
    String email;
    String password;

    public static UserCred fromUser(User user){
        return new UserCred(user.getEmail(), user.getPassword());
    }
}