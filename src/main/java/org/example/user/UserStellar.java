package org.example.user;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.User;
import org.example.models.UserCred;
import static io.restassured.RestAssured.given;

public class UserStellar {

    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String DELETE_CHANGING_USER = "/api/auth/user";

    @Step("Send POST request to api/auth/register - создание пользователя")
    public Response create(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(CREATE_USER);
    }

    @Step("Send POST request to api/auth/login - авторизация пользователя c почтой и паролем")
    public Response login(UserCred userCred){
        return given()
                .header("Content-type", "application/json")
                .body(userCred)
                .post(LOGIN_USER);
    }

    @Step("Send POST request to api/auth/login - получение токена у юзера с почтой, паролем и именем")
    public String getToken(User user){
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(LOGIN_USER);
        return response.jsonPath().getString("accessToken");
    }

    @Step("Send DELETE request to api/auth/user - удаление пользователя")
    public Response delete(User user){
        String token = getToken(user);
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete(DELETE_CHANGING_USER);
    }

    @Step("Send PATCH request to api/auth/user - изменение данных пользователя с авторизацией")
    public Response ChangingDataWithAuthorition(User user, String token){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(user)
                .patch(DELETE_CHANGING_USER);
    }

    @Step("Send PATCH request to api/auth/user - изменение данных пользователя без авторизации")
    public Response ChangingDataWithoutLogin(User user){
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .patch(DELETE_CHANGING_USER);
    }
}