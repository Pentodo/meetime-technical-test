package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dtos.WebhookEvent;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.WebhookService;

import javax.inject.Inject;

public class WebhookController extends Controller {
    final WebhookService webhookService;

    @Inject
    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    public Result handle(Http.Request request) {
        final JsonNode body = request.body().asJson();

        if (body == null || !body.isArray()) {
            return badRequest();
        }

        final String signature = request.headers().get("X-HubSpot-Signature").orElse(null);

        if (signature == null || !webhookService.validateSignature(signature, body)) {
            return unauthorized();
        }

        for (JsonNode node : body) {
            WebhookEvent event = Json.fromJson(node, WebhookEvent.class);

            // TODO: save the contact in the database
            System.out.println("Subscription Type: " + event.getSubscriptionType());
            System.out.println("Object ID: " + event.getObjectId());
        }

        return ok();
    }
}
