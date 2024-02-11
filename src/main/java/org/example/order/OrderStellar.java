package org.example.order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.models.Order;
import static io.restassured.RestAssured.given;

public class OrderStellar {

    private static final String GET_INGREDIENTS = "/api/ingredients";
    private static final String CREATE_ORDER_GET_AND_ORDER_FROM_USER = "/api/orders";
    private static final String GET_ALL_ORDER = "/api/orders/all";

    @Step("Send GET request to /api/ingredients - получение ингредиентов")
    public Response getIngredients(){
        return  given()
                .get(GET_INGREDIENTS);
    }

    @Step("Send POST request to /api/orders - создание заказа c авторизацией")
    public Response createOrderWithAuthorization(Order order, String token){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .post(CREATE_ORDER_GET_AND_ORDER_FROM_USER);
    }

    @Step("Send POST request to /api/orders - создание заказа без авторизации")
    public Response createOrderWithoutLogin(Order order){
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(CREATE_ORDER_GET_AND_ORDER_FROM_USER);
    }

    @Step("Send GET request to /api/orders - получение заказа пользователя")
    public Response getOrderWithAuthorization(String token){
        return given()
                .header("Authorization", token)
                .get(CREATE_ORDER_GET_AND_ORDER_FROM_USER);
    }

    @Step("Send GET request to /api/orders - получение заказа пользователя без авторизации")
    public Response getOrderWithoutAuthorization(){
        return given()
                .get(CREATE_ORDER_GET_AND_ORDER_FROM_USER);
    }
}