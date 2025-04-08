package actions;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.CacheService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class CheckRateLimit extends Action.Simple {
    private final CacheService cacheService;

    @Inject
    public CheckRateLimit(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        final String refreshToken = request.session().get("refreshToken").orElse(null);

        if (refreshToken != null && !cacheService.allowRequest(refreshToken)) {
            return delegate
                    .call(request)
                    .thenApply(result -> status(429, "You've reached your request limit!"));
        }

        return delegate.call(request);
    }
}
