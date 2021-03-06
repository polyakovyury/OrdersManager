package controllers;

import models.Computer;
import models.Section;
import models.Category;

import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import repository.CompanyRepository;
import repository.ComputerRepository;
import repository.SectionRepository;
import repository.CategoryRepository;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * Manage a database of computers
 */
public class HomeController extends Controller {

    private final ComputerRepository computerRepository;
    private final CompanyRepository companyRepository;
    private final SectionRepository sectionRepository;
    private final CategoryRepository categoryRepository;
    private final FormFactory formFactory;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public HomeController(FormFactory formFactory,
                          ComputerRepository computerRepository,
                          CompanyRepository companyRepository,
                          SectionRepository sectionRepository,
                          CategoryRepository categoryRepository,
                          HttpExecutionContext httpExecutionContext) {
        this.computerRepository = computerRepository;
        this.formFactory = formFactory;
        this.companyRepository = companyRepository;
        this.sectionRepository = sectionRepository;
        this.categoryRepository = categoryRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    /**
     * This result directly redirect to application home.
     */
    private Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );

    /**
     * Handle default path requests, redirect to computers list
     */
    @Security.Authenticated(Secured.class)
    public Result index() {
        return GO_HOME;
    }

    /**
     * Display the paginated list of computers.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on computer names
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> list(int page, String sortBy, String order, String filter) {
        // Run a db operation in another thread (using DatabaseExecutionContext)
        return computerRepository.page(page, 10, sortBy, order, filter).thenApplyAsync(list -> {
            // This is the HTTP rendering thread context
            return ok(views.html.list.render(list, sortBy, order, filter));
        }, httpExecutionContext.current());
    }

    /**
     * Display the 'edit form' of a existing Computer.
     *
     * @param id Id of the computer to edit
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> edit(Long id) {

        // Run a db operation in another thread (using DatabaseExecutionContext)
        CompletionStage<Map<String, String>> companiesFuture = companyRepository.options();

        // Run the lookup also in another thread, then combine the results:
        return computerRepository.lookup(id).thenCombineAsync(companiesFuture, (computerOptional, companies) -> {
            // This is the HTTP rendering thread context
            Computer c = computerOptional.get();
            Form<Computer> computerForm = formFactory.form(Computer.class).fill(c);
            return ok(views.html.editForm.render(id, computerForm, companies));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the computer to edit
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> update(Long id) throws PersistenceException {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
        if (computerForm.hasErrors()) {
            // Run companies db operation and then render the failure case
            return companyRepository.options().thenApplyAsync(companies -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.editForm.render(id, computerForm, companies));
            }, httpExecutionContext.current());
        } else {
            Computer newComputerData = computerForm.get();
            // Run update operation and then flash and then redirect
            return computerRepository.update(id, newComputerData).thenApplyAsync(data -> {
                // This is the HTTP rendering thread context
                flash("success", "Computer " + newComputerData.name + " has been updated");
                return GO_HOME;
            }, httpExecutionContext.current());
        }
    }

    /**
     * Display the 'new computer form'.
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> create() {
        Form<Computer> computerForm = formFactory.form(Computer.class);
        // Run companies db operation and then render the form
        return companyRepository.options().thenApplyAsync((Map<String, String> companies) -> {
            // This is the HTTP rendering thread context
            return ok(views.html.createForm.render(computerForm, companies));
        }, httpExecutionContext.current());
    }

    /**
     * Handle the 'new computer form' submission
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> save() {
        Form<Computer> computerForm = formFactory.form(Computer.class).bindFromRequest();
        if (computerForm.hasErrors()) {
            // Run companies db operation and then render the form
            return companyRepository.options().thenApplyAsync(companies -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.createForm.render(computerForm, companies));
            }, httpExecutionContext.current());
        }

        Computer computer = computerForm.get();
        // Run insert db operation, then redirect
        return computerRepository.insert(computer).thenApplyAsync(data -> {
            // This is the HTTP rendering thread context
            flash("success", "Computer " + computer.name + " has been created");
            return GO_HOME;
        }, httpExecutionContext.current());
    }

    /**
     * Handle computer deletion
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> delete(Long id) {
        // Run delete db operation, then redirect
        return computerRepository.delete(id).thenApplyAsync(v -> {
            // This is the HTTP rendering thread context
            flash("success", "Computer has been deleted");
            return GO_HOME;
        }, httpExecutionContext.current());
    }


    /**
     * List sections and add section form
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> sections() {
        Form<Section> sectionForm = formFactory.form(Section.class);
        return sectionRepository.options().thenApplyAsync(results -> {
            // This is the HTTP rendering thread context
            return ok(views.html.sectionForm.render(sectionForm, results));
        }, httpExecutionContext.current());
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> addSection() {
        Form<Section> sectionForm = formFactory.form(Section.class).bindFromRequest();
        if (sectionForm.hasErrors()) {
            // Run companies db operation and then render the form
            return sectionRepository.options().thenApplyAsync(results -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.sectionForm.render(sectionForm, results));
            }, httpExecutionContext.current());
        }

        Section section = sectionForm.get();
        // Run insert db operation, then redirect
        return sectionRepository.insert(section).thenApplyAsync(id -> {
            // This is the HTTP rendering thread context
            flash("success", "Section " + section.name + " has been created");
            return GO_HOME;
        }, httpExecutionContext.current());
    }

    /**
     * List categories and add category form
     */
    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> listCategories() {
        return categoryRepository.options().thenApplyAsync(results -> {
            // This is the HTTP rendering thread context
            return ok(views.html.categoryList.render(results));
        }, httpExecutionContext.current());
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> createCategory() {
        Form<Category> categoryForm = formFactory.form(Category.class);
        return sectionRepository.options().thenApplyAsync(results -> {
            // This is the HTTP rendering thread context
            return ok(views.html.categoryForm.render(categoryForm, results));
        }, httpExecutionContext.current());
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> saveCategory() {
        Form<Category> form = formFactory.form(Category.class).bindFromRequest();
        if (form.hasErrors()) {
            return sectionRepository.options().thenApplyAsync(results -> {
                // This is the HTTP rendering thread context
                return badRequest(views.html.categoryForm.render(form, results));
            }, httpExecutionContext.current());
        }

        Category category = form.get();
        // Run insert db operation, then redirect
        return categoryRepository.insert(category).thenApplyAsync(id -> {
            // This is the HTTP rendering thread context
            flash("success", "Category " + category.name + " " + category.section.id + " has been created");
            return GO_HOME;
        }, httpExecutionContext.current());
    }
}
            
