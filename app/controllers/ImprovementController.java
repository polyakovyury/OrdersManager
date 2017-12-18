package controllers;

import models.Improvement;
import repository.ImprovementRepository;

import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Manage improvements
 */
public class ImprovementController extends Controller {

    private final ImprovementRepository repository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public ImprovementController(FormFactory formFactory,
                          ImprovementRepository repository,
                          HttpExecutionContext httpExecutionContext) {
        this.formFactory = formFactory;
        this.repository = repository;
        this.httpExecutionContext = httpExecutionContext;
    }

    /**
     * This result directly redirect to application home.
     */
    private Result GO_HOME = Results.redirect(
        routes.HomeController.list(0, "name", "asc", "")
    );

    /**
     * List improvements and add improvement form
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> list() {
        Form<Improvement> form = formFactory.form(Improvement.class);
        return repository.options().thenApplyAsync(results -> {
            // This is the HTTP rendering thread context
            return ok(views.html.improvementForm.render(form, results));
        }, httpExecutionContext.current());
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> save() {
        Form<Improvement> form = formFactory.form(Improvement.class).bindFromRequest();
        if (form.hasErrors()) {
            // Run companies db operation and then render the form
            return repository.options().thenApplyAsync(results -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.improvementForm.render(form, results));
            }, httpExecutionContext.current());
        }

        Improvement improvement = form.get();
        // Run insert db operation, then redirect
        return repository.insert(improvement).thenApplyAsync(id -> {
            // This is the HTTP rendering thread context
            flash("success", "Improvement " + improvement.name + " has been created");
            return GO_HOME;
        }, httpExecutionContext.current());
    }
}