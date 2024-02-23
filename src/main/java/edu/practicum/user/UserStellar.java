package edu.practicum.user;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import edu.practicum.models.User;
import edu.practicum.models.UserCred;
import static io.restassured.RestAssured.given;

public class UserStellar {

    private static final String CREATE_USER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String DELETE_CHANGING_USER = "/api/auth/user";

    @Step("POST /api/auth/register - создание пользователя")
    public Response create(User user){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(user)
                .post(CREATE_USER);
    }

    @Step("POST /api/auth/login - авторизация пользователя(почта/пароль")
    public Response login(UserCred userCred){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(userCred)
                .post(LOGIN);
    }

    @Step("POST  /api/auth/login - получение токена")
    public String getToken(User user){
        Response response = given()
                .log().all()
                .header("Content-type", "application/json")
                .body(user)
                .post(LOGIN);
        return response.jsonPath().getString("accessToken");
    }

    @Step("DELETE /api/auth/user - удаление пользователя")
    public void delete(User user){
        String token = getToken(user);
        given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete(DELETE_CHANGING_USER);
    }

    @Step("PATCH /api/auth/user - изменение данных пользователя + авторизация")
    public Response ChangingDataWithAuthorition(User user, String token){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(user)
                .patch(DELETE_CHANGING_USER);
    }

    @Step("PATCH /api/auth/user - изменение данных пользователя")
    public Response ChangingDataWithoutLogin(User user){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(user)
                .patch(DELETE_CHANGING_USER);
    }
}