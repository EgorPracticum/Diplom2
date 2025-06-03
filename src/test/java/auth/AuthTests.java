package auth;

import api.clients.AuthClient;
import api.clients.UserClient;
import api.models.User;
import api.utils.TestDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class AuthTests {
    private final AuthClient authClient = new AuthClient();
    private final UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void loginWithValidCredentialsSuccessfully() {
        User user = TestDataGenerator.generateRandomUser();
        userClient.createUser(user);

        Response response = authClient.login(user);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Авторизация с неверными учетными данными")
    public void loginWithInvalidCredentialsFails() {
        User invalidUser = User.builder()
                .email("nonexistent@test.com")
                .password("wrongpassword")
                .build();

        Response response = authClient.login(invalidUser);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с пустым паролем")
    public void loginWithEmptyPasswordFails() {
        User user = User.builder()
                .email("test@example.com")
                .password("")
                .build();

        Response response = authClient.login(user);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Выход из системы")
    public void logoutSuccessfully() {
        User user = TestDataGenerator.generateRandomUser();
        userClient.createUser(user);
        Response loginResponse = authClient.login(user);
        String refreshToken = loginResponse.path("refreshToken");

        Response logoutResponse = authClient.logout(refreshToken);
        logoutResponse.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }
}