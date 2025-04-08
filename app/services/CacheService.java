package services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dtos.TokenResponse;
import org.apache.pekko.Done;
import play.cache.AsyncCacheApi;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class CacheService {
    private final AsyncCacheApi cache;
    private final Cache<String, AtomicInteger> requestCache;


    @Inject
    public CacheService(AsyncCacheApi cache) {
        this.cache = cache;
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).build();
    }

    public CompletionStage<Done> saveAccessToken(TokenResponse dto) {
        return cache.set(dto.getRefreshToken(), dto.getAccessToken(), dto.getExpiresIn());
    }

    public CompletionStage<Optional<String>> getAccessToken(String refreshToken) {
        return cache.get(refreshToken);
    }

    public CompletionStage<Optional<String>> getAccessToken(Http.Request request) {
        final String refreshToken = request.session().get("refreshToken").orElse(null);

        return getAccessToken(refreshToken);
    }

    public CompletionStage<Done> deleteAccessToken(String refreshToken) {
        return cache.remove(refreshToken);
    }

    public boolean allowRequest(String refreshToken) {
        AtomicInteger count = requestCache.getIfPresent(refreshToken);

        if (count == null) {
            count = new AtomicInteger(0);
            requestCache.put(refreshToken, count);
        }

        // for OAuth apps, each HubSpot account is limited to 110 requests every 10 seconds
        return count.incrementAndGet() <= 110;
    }
}
