package controllers;

import annotations.WithRefreshToken;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import services.CacheService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class AuthController extends Controller {
    private final AuthService authService;
    private final CacheService cacheService;

    @Inject
    public AuthController(AuthService authService, CacheService cacheService) {
        this.authService = authService;
        this.cacheService = cacheService;
    }

    public CompletionStage<Result> auth(Http.Request request) {
        final String refreshToken = request.session().get("refreshToken").orElse(null);

        if (refreshToken == null) {
            return completedFuture(ok(views.html.auth.render(authService.getInstallUrl(), null)));
        }

        return cacheService.getAccessToken(refreshToken).thenCompose(accessToken -> {
            if (accessToken.isPresent()) {
                return completedFuture(ok(views.html.auth.render(null, accessToken.get())));
            }

            return authService.fetchAndCacheOAuthTokens(authService.getRefreshUrl(refreshToken))
                    .thenApply(newRefreshToken -> redirect("/").addingToSession(request, "refreshToken", newRefreshToken))
                    .exceptionally(e -> status(500, e.getMessage()));
        });
    }

    public CompletionStage<Result> callback(String code, Http.Request request) {
        return authService.fetchAndCacheOAuthTokens(authService.getAuthorizationUrl(code))
                .thenApply(refreshToken -> redirect("/").addingToSession(request, "refreshToken", refreshToken))
                .exceptionally(e -> status(500, e.getMessage()));
    }

    @WithRefreshToken
    public CompletionStage<Result> logout(Http.Request request) {
        final String refreshToken = request.session().get("refreshToken").orElse(null);

        return cacheService
                .deleteAccessToken(refreshToken)
                .thenApply(done -> redirect("/").removingFromSession(request, "refreshToken"));
    }
}
