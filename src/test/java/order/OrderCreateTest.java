package order;

import com.github.javafaker.Faker;
import edu.practicum.models.Order;
import edu.practicum.models.User;
import edu.practicum.order.OrderStellar;
import edu.practicum.user.UserStellar;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static edu.practicum.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static edu.practicum.user.UserGenerator.randomUser;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest {

    private String[] id;
    private List<String> ingredients;

    private final OrderStellar orderStellar = new OrderStellar();
    private final UserStellar userStellar = new UserStellar();
    private final Faker faker = new Faker();
    private final User user = randomUser();

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;
        userStellar.create(user);
        id = orderStellar.getIngredients().getBody().jsonPath().getString("data._id").split(",");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithLoginOkTest(){
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        String[] arrayIngredients = ingredients.toArray(new String[0]);
        Order order = new Order(arrayIngredients);
        String token = userStellar.getToken(user);

        Response response = orderStellar.createOrderWithAuthorization(order, token);
        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("order.ingredients[0]._id", equalTo(arrayIngredients[0]));
    }

    @Test
    @DisplayName("Создание заказа без авторизации - невозможно")
    public void createOrderWithoutLoginOkTest(){
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        String[] arrayIngredients = ingredients.toArray(new String[0]);
        Order order = new Order(arrayIngredients);

        Response response = orderStellar.createOrderWithoutLogin(order);
        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов - невозможно")
    public void createOrderWithoutIngredientsNotOkTest(){
        String[] ingredient = new String[0];
        Order order = new Order(ingredient);

        Response response = orderStellar.createOrderWithoutLogin(order);
        response.then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов - невозможно")
    public void createOrderWithUnrealHashNotOkTest(){
        String id = faker.letterify("????????????????????");
        String[] ingredientFaker = new String[]{id};
        Order order = new Order(ingredientFaker);
        String token = userStellar.getToken(user);

        Response response = orderStellar.createOrderWithAuthorization(order, token);
        response.then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}