package api.clients;

import api.models.AuthToken;
import api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static api.utils.ApiEndpoint.*;
import static io.restassured.RestAssured.given;

public class AuthClient {
    @Step("Авторизация пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_LOGIN);
    }

    @Step("Выход из системы")
    public Response logout(String refreshToken) {
        AuthToken token = new AuthToken(refreshToken);

        return given()
                .header("Content-type", "application/json")
                .body(token)
                .when()
                .post(USER_LOGOUT);
    }
}