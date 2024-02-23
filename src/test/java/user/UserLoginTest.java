package user;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import edu.practicum.models.User;
import edu.practicum.models.UserCred;
import edu.practicum.user.UserStellar;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static edu.practicum.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static edu.practicum.user.UserGenerator.randomUser;
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
    @DisplayName("Логин под существующим пользователем - ок")
    public void loginRealUserOkTest(){

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
    @DisplayName("Логин с неверным логином - не ок")
    public void loginUnrealEmailNotOkTest(){
        userForLogin.setEmail("skbr-@yandex.ru");

        Response response = userStellar.login(userForLogin);
        response
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем - не ок")
    public void loginUnrealPasswordNotOkTest(){
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