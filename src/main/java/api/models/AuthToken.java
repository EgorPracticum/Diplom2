package api.models;

import lombok.Data;

@Data
public class AuthToken {
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }
}