package user;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.models.User;
import org.example.models.UserCred;
import org.example.user.UserStellar;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.example.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static org.example.user.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {

    UserStellar userStellar = new UserStellar();
    User user = randomUser();
    UserCred userForLogin = UserCred.fromUser(user);

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;
        userStellar.create(user);
    }

    @Test
    @DisplayName("Логин под существующим пользователем - возможен")
    public void loginRealUserPossible(){

        Response response = userStellar.login(userForLogin);
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", IsEqual.equalTo(user.getEmail()))
                .body("user.name", IsEqual.equalTo(user.getName()));
    }

    @Test
    @DisplayName("Логин с неверным логином - не возможен")
    public void loginUnrealEmailImpossible(){
        userForLogin.setEmail("skbr-@yandex.ru");

        Response response = userStellar.login(userForLogin);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем - не возможен")
    public void loginUnrealPasswordImpossible(){
        userForLogin.setPassword("dornyve34");

        Response response = userStellar.login(userForLogin);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}