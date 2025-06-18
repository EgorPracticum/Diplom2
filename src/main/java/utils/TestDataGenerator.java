package api.utils;

import api.clients.OrderClient;
import api.models.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TestDataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static User generateRandomUser() {
        return User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(8, 12) + "A1!")
                .name(faker.name().fullName())
                .build();
    }

    public static List<String> getValidIngredients(OrderClient orderClient) {
        Response response = orderClient.getIngredients();
        List<Object> ingredients = response.jsonPath().getList("data._id");

        return ingredients.stream()
                .map(Object::toString)
                .limit(3)
                .collect(Collectors.toList());
    }

    public static List<String> getInvalidIngredients() {
        return List.of(
                "invalid" + faker.random().hex(10),
                "invalid" + faker.random().hex(10)
        );
    }
}