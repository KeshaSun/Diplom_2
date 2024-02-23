package order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import edu.practicum.models.Order;
import edu.practicum.models.User;
import edu.practicum.order.OrderStellar;
import edu.practicum.user.UserStellar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static edu.practicum.models.StellarBurgersUrl.STELLAR_BURGERS_URL;
import static edu.practicum.user.UserGenerator.randomUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderGetTest {

   private List<String> ingredients;
    private final OrderStellar orderStellar = new OrderStellar();
    private final UserStellar userStellar = new UserStellar();
    private final User user = randomUser();

    @Before
    public void setUp(){
        RestAssured.baseURI = STELLAR_BURGERS_URL;

        //Создаем пользователя и получаем его айди
        userStellar.create(user);
        String token = userStellar.getToken(user);

        //Получаем айди ингредиента, добавляем ингредиент в заказ
        String[] id = orderStellar.getIngredients().getBody().jsonPath().getString("data._id").split(",");
        ingredients = List.of(id[4].trim(), id[3].trim(), id[2].trim());
        String[] arrayIngredients = ingredients.toArray(new String[0]);
        Order order = new Order(arrayIngredients);

        //Авторизованный пользователь создает заказ
        orderStellar.createOrderWithAuthorization(order, token);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя - возможно")
    public void getOrderFromAuthorizedUserOkTest(){
        String token = userStellar.getToken(user);

        Response response = orderStellar.getOrderWithAuthorization(token);
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя - невозможно")
    public void getOrderFromNotAuthorizedUserNotOkTest(){

        Response response = orderStellar.getOrderWithoutAuthorization();
        response
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown(){
        userStellar.delete(user);
    }
}