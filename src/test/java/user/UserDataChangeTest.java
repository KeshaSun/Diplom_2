package user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.models.User;
import org.example.user.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.example.Utils.Utils.randomString;
import static org.example.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static org.example.user.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataChangeTest {

    UserStellar userStellar = new UserStellar();
    User user = randomUser();
    Faker faker = new Faker();

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;
        userStellar.create(user);
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией - возможно")
    public void changeEmailWithLoginPossible(){
        String token = userStellar.getToken(user);
        user.setEmail(faker.internet().emailAddress());

        Response response = userStellar.ChangingDataWithAuthorition(user, token);
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение password пользователя с авторизацией - возможно")
    public void changePasswordWithLoginPossible(){
        String token = userStellar.getToken(user);
        user.setPassword(faker.bothify("56???????"));
        Response response = userStellar.ChangingDataWithAuthorition(user, token);

        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение name пользователя с авторизацией - возможно")
    public void changeNameWithLoginPossible(){
        String token = userStellar.getToken(user);
        user.setName(randomString(5));
        Response response = userStellar.ChangingDataWithAuthorition(user, token);

        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации - невозможно")
    public void changeEmailWithoutLoginImpossible(){
        User user2 = new User(user);
        user2.setEmail(faker.internet().emailAddress());

        Response response = userStellar.ChangingDataWithoutLogin(user2);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение password пользователя без авторизации - невозможно")
    public void changePasswordWithoutLoginImpossible(){
        User user2 = new User(user);
        user2.setPassword(faker.bothify("56???????"));

        Response response = userStellar.ChangingDataWithoutLogin(user2);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение name пользователя без авторизации - невозможно")
    public void changeNameWithoutLoginImpossible(){
        User user2 = new User(user);
        user2.setName(randomString(5));

        Response response = userStellar.ChangingDataWithoutLogin(user2);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}
