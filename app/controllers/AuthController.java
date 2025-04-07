package controllers;

import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import services.AuthService;

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

    public Result auth() {
        return ok(views.html.auth.render(authService.getAuthorizationUrl()));
    }

    public CompletionStage<Result> callback(String code) {
        return ws.url(authService.getTokenUrl(code))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post("")
                .thenApply(response -> status(response.getStatus(), response.getBody()));
    }
}
