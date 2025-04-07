package services;

import dtos.TokenResponse;
import org.apache.pekko.Done;
import play.cache.AsyncCacheApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@Singleton
public class CacheService {
    private final AsyncCacheApi cache;

    @Inject
    public CacheService(AsyncCacheApi cache) {
        this.cache = cache;
    }

    public CompletionStage<Done> saveAccessToken(TokenResponse dto) {
        return cache.set(dto.getRefreshToken(), dto.getAccessToken(), dto.getExpiresIn());
    }

    public CompletionStage<Optional<String>> getAccessToken(String refreshToken) {
        return cache.get(refreshToken);
    }

    public CompletionStage<Done> deleteAccessToken(String refreshToken) {
        return cache.remove(refreshToken);
    }
}
