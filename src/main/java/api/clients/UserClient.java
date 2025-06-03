package api.clients;

import api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.utils.ApiEndpoint.*;
import static io.restassured.RestAssured.given;

public class UserClient {
    @Step("Создание пользователя")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_REGISTER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DELETE);
    }

    @Step("Обновление данных пользователя")
    public Response updateUser(User user, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .patch(USER_UPDATE);
    }
}