package services;

import com.typesafe.config.Config;
import dtos.TokenResponse;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class AuthService {
    private final String installUrl;
    private final String tokenUrl;
    private final WSClient ws;
    private final CacheService cacheService;

    @Inject
    public AuthService(Config config, WSClient ws, CacheService cacheService) {
        final String clientId = config.getString("auth.clientId");
        final String clientSecret = config.getString("auth.clientSecret");
        final String redirectUri = config.getString("auth.redirectUri");
        final String scope = String.join("%20", config.getString("auth.scope").split(","));

        this.installUrl = "https://app.hubspot.com/oauth/authorize?" +
                "client_id=" + clientId + "&" +
                "redirect_uri=" + redirectUri + "&" +
                "scope=" + scope;
        this.tokenUrl = "https://api.hubapi.com/oauth/v1/token?" +
                "client_id=" + clientId + "&" +
                "client_secret=" + clientSecret + "&" +
                "redirect_uri=" + redirectUri;
        this.ws = ws;
        this.cacheService = cacheService;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public String getAuthorizationUrl(String code) {
        return tokenUrl + "&grant_type=authorization_code&code=" + code;
    }

    public String getRefreshUrl(String refreshToken) {
        return tokenUrl + "&grant_type=refresh_token&refresh_token=" + refreshToken;
    }

    public CompletionStage<String> fetchAndCacheOAuthTokens(String url) {
        return ws.url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post("")
                .thenCompose(response -> {
                    final String body = response.getBody();

                    if (response.getStatus() != 200) {
                        return CompletableFuture.failedFuture(new Exception(body));
                    }

                    try {
                        TokenResponse dto = new TokenResponse(body);
                        // returning refreshToken because it is needed to identify the cache
                        return cacheService.saveAccessToken(dto).thenApply(done -> dto.getRefreshToken());
                    } catch (Exception e) {
                        return CompletableFuture.failedFuture(e);
                    }
                });
    }
}
