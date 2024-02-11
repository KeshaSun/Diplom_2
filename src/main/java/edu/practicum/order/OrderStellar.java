package edu.practicum.order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import edu.practicum.models.Order;
import static io.restassured.RestAssured.given;

public class OrderStellar {

    private static final String GET_INGREDIENTS = "/api/ingredients";
    private static final String ORDER_FROM_USER = "/api/orders";

    @Step("GET /api/ingredients - получение ингредиентов")
    public Response getIngredients(){
        return  given()
                .log().all()
                .get(GET_INGREDIENTS);
    }
    @Step("POST /api/orders - создание заказа")
    public Response createOrderWithoutLogin(Order order){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(order)
                .post(ORDER_FROM_USER);
    }
    @Step("POST /api/orders - создание заказа + авторизация")
    public Response createOrderWithAuthorization(Order order, String token){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .post(ORDER_FROM_USER);
    }
    @Step("GET /api/orders - получение заказа")
    public Response getOrderWithoutAuthorization(){
        return given()
                .log().all()
                .get(ORDER_FROM_USER);
    }
    @Step("GET /api/orders - получение заказа + авторизация")
    public Response getOrderWithAuthorization(String token){
        return given()
                .log().all()
                .header("Authorization", token)
                .get(ORDER_FROM_USER);
    }


}