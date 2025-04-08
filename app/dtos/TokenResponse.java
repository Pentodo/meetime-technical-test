package dtos;

import com.fasterxml.jackson.databind.JsonNode;

public class TokenResponse {
    private final String refreshToken;
    private final String accessToken;
    private final int expiresIn;

    public TokenResponse(JsonNode body) throws Exception {
        this.refreshToken = body.get("refresh_token").asText();
        this.accessToken = body.get("access_token").asText();
        // multiplied by 0.9 to (over) simulate the time spent on the request
        this.expiresIn = (int) (body.get("expires_in").asInt() * 0.9);
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
