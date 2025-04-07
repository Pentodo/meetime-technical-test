package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthService;
import views.html.auth;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AuthController extends Controller {
    private final AuthService authService;
    private final WSClient ws;

    @Inject
    public AuthController(WSClient ws, AuthService authService) {
        this.authService = authService;
        this.ws = ws;
    }

    public Result auth(Http.Request request) {
        final String accessToken = request.session().get("accessToken").orElse(null);
        final String authorizationUrl = accessToken == null ? authService.getAuthorizationUrl() : null;

        return ok(auth.render(authorizationUrl, accessToken));
    }

    public CompletionStage<Result> callback(String code, Http.Request request) {
        return ws.url(authService.getTokenUrl(code))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post("")
                .thenApply(response -> {
                    if (response.getStatus() != 200) {
                        return status(response.getStatus(), response.getBody());
                    }

                    try {
                        return redirect("/").addingToSession(
                                request,
                                "accessToken",
                                Json.mapper().readTree(response.getBody()).get("access_token").asText()
                        );
                    } catch (JsonProcessingException e) {
                        return status(500, e.getMessage());
                    }
                });
    }

    public Result logout(Http.Request request) {
        return redirect("/").removingFromSession(request, "accessToken");
    }
}
