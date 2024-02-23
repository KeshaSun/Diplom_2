package edu.practicum.models;
import lombok.*;

@Data
@AllArgsConstructor
public class UserCred {
    String email;
    String password;

    public static UserCred fromUser(User user){
        return new UserCred(user.getEmail(), user.getPassword());
    }
}