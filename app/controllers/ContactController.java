package controllers;

import annotations.WithRefreshToken;
import play.mvc.Controller;
import play.mvc.Result;

public class ContactController extends Controller {

    @WithRefreshToken
    public Result contact() {
        return ok(views.html.contact.render(null));
    }
}
