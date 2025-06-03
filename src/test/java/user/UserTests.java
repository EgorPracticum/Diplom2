package user;

import api.clients.UserClient;
import api.models.User;
import api.utils.TestDataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserTests {
    private final UserClient userClient = new UserClient();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccessfully() {
        User user = TestDataGenerator.generateRandomUser();
        Response response = userClient.createUser(user);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDuplicateUserFails() {
        User user = TestDataGenerator.generateRandomUser();
        Response firstResponse = userClient.createUser(user);
        accessToken = firstResponse.path("accessToken");

        Response secondResponse = userClient.createUser(user);
        secondResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Изменение данных пользователя")
    public void updateUserDataSuccessfully() {
        User user = TestDataGenerator.generateRandomUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");

        User updatedUser = User.builder()
                .email("updated" + System.currentTimeMillis() + "@test.com")
                .password("newpassword123")
                .name("Updated Name")
                .build();

        Response updateResponse = userClient.updateUser(updatedUser, accessToken);
        updateResponse.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail()))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void updateUserWithoutAuthFails() {
        User user = TestDataGenerator.generateRandomUser();
        Response createResponse = userClient.createUser(user);
        accessToken = createResponse.path("accessToken");

        User updatedUser = TestDataGenerator.generateRandomUser();
        Response response = userClient.updateUser(updatedUser, "");

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void createUserWithoutEmailFails() {
        User user = User.builder()
                .password("password")
                .name("name")
                .build();

        Response response = userClient.createUser(user);
        response.then()
                .statusCode(403)
                .body("success", equalTo(false));
    }
}