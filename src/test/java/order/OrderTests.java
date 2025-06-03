package order;

import api.clients.OrderClient;
import api.clients.UserClient;
import api.models.User;
import api.utils.TestDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class OrderTests {
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String accessToken;
    private List<String> validIngredients;

    @Before
    public void setUp() {
        User user = TestDataGenerator.generateRandomUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");


        Response ingredientsResponse = orderClient.getIngredients();
        ingredientsResponse.then()
                .statusCode(200)
                .body("data", not(empty()));

        validIngredients = ingredientsResponse.jsonPath().getList("data._id");
        assertTrue("Должно быть доступно минимум 2 ингредиента", validIngredients.size() >= 2);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthSuccessfully() {
        List<String> ingredients = validIngredients.subList(0, 2);

        Response response = orderClient.createOrder(ingredients, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        List<String> ingredients = validIngredients.subList(0, 2);

        Response response = orderClient.createOrder(ingredients, "");
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Response response = orderClient.createOrder(List.of(), accessToken);
        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверными ингредиентами")
    public void createOrderWithInvalidIngredients() {
        List<String> invalidIngredients = TestDataGenerator.getInvalidIngredients();

        Response response = orderClient.createOrder(invalidIngredients, accessToken);
        response.then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void getUserOrdersWithAuth() {

        List<String> ingredients = validIngredients.subList(0, 2);
        orderClient.createOrder(ingredients, accessToken);

        Response response = orderClient.getUserOrders(accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", not(empty()));
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void getUserOrdersWithoutAuth() {
        Response response = orderClient.getUserOrders("");
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}