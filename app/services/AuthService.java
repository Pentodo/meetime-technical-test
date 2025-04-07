package services;

import com.typesafe.config.Config;

import javax.inject.Inject;

public class AuthService {
    private final String authorizationUrl;
    private final String tokenUrl;

    @Inject
    public AuthService(Config config) {
        final String clientId = config.getString("auth.clientId");
        final String clientSecret = config.getString("auth.clientSecret");
        final String redirectUri = config.getString("auth.redirectUri");
        final String scope = String.join("%20", config.getString("auth.scope").split(","));

        this.authorizationUrl = "https://app.hubspot.com/oauth/authorize?" +
                "client_id=" + clientId + "&" +
                "redirect_uri=" + redirectUri + "&" +
                "scope=" + scope;
        this.tokenUrl = "https://api.hubapi.com/oauth/v1/token?grant_type=authorization_code&" +
                "client_id=" + clientId + "&" +
                "client_secret=" + clientSecret + "&" +
                "redirect_uri=" + redirectUri;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public String getTokenUrl(String code) {
        return tokenUrl + "&code=" + code;
    }
}
