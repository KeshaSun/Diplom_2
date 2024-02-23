package user;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import edu.practicum.models.User;
import edu.practicum.user.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static edu.practicum.utils.Utils.randomString;
import static edu.practicum.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static edu.practicum.user.UserGenerator.randomUser;
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
    @DisplayName("Изменение email пользователя + авторизация - ок")
    public void changeEmailWithLoginOkTest(){
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
    @DisplayName("Изменение пароля пользователя + авторизация - ок")
    public void changePasswordWithLoginOkTest(){
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
    @DisplayName("Изменение name пользователя + авторизация - ок")
    public void changeNameWithLoginOkTest(){
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
    @DisplayName("Изменение почты пользователя - авторизация - не ок")
    public void changeEmailWithoutLoginNotOkTest(){
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
    @DisplayName("Изменение пароля пользователя - авторизация - не ок")
    public void changePasswordWithoutLoginNotOkTest(){
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
    @DisplayName("Изменение имени пользователя - авторизация - не ок")
    public void changeNameWithoutLoginNotOkTest(){
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
