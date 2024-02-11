package order;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.example.models.Order;
import org.example.models.User;
import org.example.order.OrderStellar;
import org.example.user.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.example.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static org.example.user.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderCreateTest {

    String[] id;
    List<String> ingredients;

    OrderStellar orderStellar = new OrderStellar();
    UserStellar userStellar = new UserStellar();
    Faker faker = new Faker();
    User user = randomUser();

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;
        userStellar.create(user);
        id = orderStellar.getIngredients().getBody().jsonPath().getString("data._id").split(",");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithLoginPossible(){
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        String[] arrayIngredients = ingredients.toArray(new String[0]);
        Order order = new Order(arrayIngredients);
        String token = userStellar.getToken(user);

        Response response = orderStellar.createOrderWithAuthorization(order, token);
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.ingredients[0]._id", equalTo(arrayIngredients[0]));
    }

    @Test
    @DisplayName("Создание заказа без авторизации - невозможно")
    public void createOrderWithoutLoginPpossible(){
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        String[] arrayIngredients = ingredients.toArray(new String[0]);
        Order order = new Order(arrayIngredients);

        Response response = orderStellar.createOrderWithoutLogin(order);
        response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов - невозможно")
    public void createOrderWithoutIngredientsImpossible(){
        String[] ingredient = new String[0];
        Order order = new Order(ingredient);

        Response response = orderStellar.createOrderWithoutLogin(order);
        response
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов - невозможно")
    public void createOrderWithUnrealHashImpossible(){
        String id = faker.letterify("????????????????????");
        String[] ingredientFaker = new String[]{id};
        Order order = new Order(ingredientFaker);
        String token = userStellar.getToken(user);

        Response response = orderStellar.createOrderWithAuthorization(order, token);
        response
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}