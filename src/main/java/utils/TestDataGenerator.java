package api.utils;

import api.models.User;

import java.util.List;

public class TestDataGenerator {
    public static User generateRandomUser() {
        long timestamp = System.currentTimeMillis();
        return User.builder()
                .email("test" + timestamp + "@test.com")
                .password("password" + timestamp)
                .name("name" + timestamp)
                .build();
    }

    public static List<String> getValidIngredients() {
        return List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa70"
        );
    }

    public static List<String> getInvalidIngredients() {
        return List.of("invalid_ingredient_1", "invalid_ingredient_2");
    }
}