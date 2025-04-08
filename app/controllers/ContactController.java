package controllers;

import annotations.WithRefreshToken;
import dtos.ContactDto;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.ContactService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ContactController extends Controller {
    private final ContactService contactService;
    private final Form<ContactDto> contactForm;

    @Inject
    public ContactController(ContactService contactService, FormFactory formFactory) {
        this.contactService = contactService;
        this.contactForm = formFactory.form(ContactDto.class);
    }

    @WithRefreshToken
    public CompletionStage<Result> contact(Http.Request request) {
        return contactService.fetchContacts(request)
                .thenApply(contacts -> ok(views.html.contact.render(contacts, contactForm, request)))
                .exceptionally(e -> status(500, e.getMessage()));
    }

    @WithRefreshToken
    public CompletionStage<Result> addContact(Http.Request request) {
        Form<ContactDto> boundForm = contactForm.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest("Invalid form submission!"));
        }

        return contactService.addContact(request, boundForm.get())
                .thenApply(v -> redirect(routes.ContactController.contact()))
                .exceptionally(e -> status(500, e.getMessage()));
    }
}
