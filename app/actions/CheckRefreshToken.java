package actions;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import services.CacheService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CheckRefreshToken extends Action.Simple {
    private final AuthService authService;
    private final CacheService cacheService;

    @Inject
    public CheckRefreshToken(AuthService authService, CacheService cacheService) {
        this.authService = authService;
        this.cacheService = cacheService;
    }

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        String refreshToken = request.session().get("refreshToken").orElse(null);

        if (refreshToken == null) {
            return CompletableFuture.completedFuture(unauthorized("You're NOT authenticated!"));
        }

        return cacheService.getAccessToken(refreshToken).thenCompose(accessToken -> {
            if (accessToken.isPresent()) {
                return delegate.call(request);
            }

            return authService.fetchAndCacheOAuthTokens(authService.getRefreshUrl(refreshToken))
                    .thenCompose(newRefreshToken -> {
                        request.session().adding("refreshToken", newRefreshToken);
                        return delegate.call(request);
                    })
                    .exceptionally(e -> status(500, e.getMessage()));
        });
    }
}
