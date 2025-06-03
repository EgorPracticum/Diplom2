package api.utils;

public class ApiEndpoint {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static final String USER_REGISTER = BASE_URL + "/api/auth/register";
    public static final String USER_LOGIN = BASE_URL + "/api/auth/login";
    public static final String USER_LOGOUT = BASE_URL + "/api/auth/logout";
    public static final String USER_UPDATE = BASE_URL + "/api/auth/user";
    public static final String USER_DELETE = BASE_URL + "/api/auth/user";

    public static final String ORDER_CREATE = BASE_URL + "/api/orders";
    public static final String ORDER_GET = BASE_URL + "/api/orders";

    public static final String INGREDIENTS = BASE_URL + "/api/ingredients";
}