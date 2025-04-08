package services;

import com.fasterxml.jackson.databind.JsonNode;
import dtos.ContactDto;
import models.Contact;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ContactService {
    private static final String CONTACTS_URL = "https://api.hubspot.com/crm/v3/objects/contacts";

    private final WSClient ws;
    private final CacheService cacheService;

    @Inject
    public ContactService(WSClient ws, CacheService cacheService) {
        this.ws = ws;
        this.cacheService = cacheService;
    }

    public CompletionStage<List<Contact>> fetchContacts(Http.Request request) {
        return cacheService.getAccessToken(request)
                .thenCompose(accessToken -> accessToken
                        .map(this::fetchFromApi)
                        .orElseGet(() -> CompletableFuture.failedFuture(new Exception("Access token not available")))
                );
    }

    private CompletionStage<List<Contact>> fetchFromApi(String token) {
        return ws.url(CONTACTS_URL)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .get()
                .thenApply(WSResponse::asJson)
                .thenApply(this::parseContactsFromResponse);
    }

    private List<Contact> parseContactsFromResponse(JsonNode response) {
        final List<Contact> contacts = new ArrayList<>();

        response.get("results").forEach(contact -> {
            final JsonNode properties = contact.get("properties");

            contacts.add(new Contact(
                    properties.get("email").asText(),
                    properties.get("firstname").asText(),
                    properties.get("lastname").asText()
            ));
        });

        return contacts;
    }

    public CompletionStage<Void> addContact(Http.Request request, ContactDto contactDto) {
        return cacheService.getAccessToken(request).thenCompose(accessToken -> accessToken
                .map(token -> addToApi(token, contactDto))
                .orElseGet(() -> CompletableFuture.failedFuture(new Exception("Access token not available")))
        );
    }

    private CompletionStage<Void> addToApi(String token, ContactDto contactDto) {
        return ws.url(CONTACTS_URL)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .post(Json.toJson(Map.of("properties", contactDto)))
                .thenApply(response -> {
                    if (response.getStatus() != 201) {
                        throw new RuntimeException(response.getBody());
                    }

                    return null;
                });
    }
}
