package api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static api.utils.ApiEndpoint.*;
import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Создание заказа")
    public Response createOrder(List<String> ingredients, String accessToken) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredients);

        RequestSpecification request = given()
                .header("Content-type", "application/json")
                .body(body);

        if (accessToken != null && !accessToken.isEmpty()) {
            request.header("Authorization", accessToken);
        }

        return request.when().post(ORDER_CREATE);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_GET);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS);
    }
}