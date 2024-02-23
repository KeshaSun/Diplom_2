package user;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import edu.practicum.models.User;
import org.apache.http.HttpStatus;
import edu.practicum.user.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static edu.practicum.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static edu.practicum.user.UserGenerator.randomUser;

public class UserCreateTest {

    UserStellar userStellar = new UserStellar();
    User user = randomUser();

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;
    }

    @Test
    @DisplayName("Создание нового пользователь")
    public void createUniqueUserOkTest(){
        Response response = userStellar.create(user);
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание пользователя, который уже существует - не возможно")
    public void createSameUserNotOkTest(){
        User userSame = new User(user);

        Response responseFirst = userStellar.create(user);
        assertEquals("Неверный статус код ответа", SC_OK, responseFirst.statusCode());
        Response responseSecond = userStellar.create(userSame);
        responseSecond
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым обязательным полем - невозможно")
    public void createUserWithEmptyFieldNotOkTest(){
        userStellar.create(user);
        User userWithEmptyEmail = new User(user);
        userWithEmptyEmail.setEmail("");

        Response response = userStellar.create(userWithEmptyEmail);
        response
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}
