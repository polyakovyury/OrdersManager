package controllers;

import formclasses.Login;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import play.mvc.Http;

import javax.inject.Inject;

/**
 * Manage a authorization process
 */
public class AuthenticationController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public AuthenticationController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result login() {

        return ok (
            views.html.loginForm.render(formFactory.form(Login.class))
        );
    }

    public Result authenticate() {

        Form<Login> form = formFactory.form(Login.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(views.html.loginForm.render(form));
        } else {
            session().clear();
            session("username", form.get().getUsername());
            return redirect(
                routes.HomeController.index()
            );
        } 
    }

    public Result logout() {
        session().clear();
        flash("success", "logout.message");
        return redirect(
            routes.AuthenticationController.login()
        );
    }
}