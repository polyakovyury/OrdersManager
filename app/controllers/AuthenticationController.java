package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;


import javax.inject.Inject;
import java.util.Map;
import java.util.HashMap;

/**
 * Manage a authorization process
 */
public class AuthenticationController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public AuthenticationController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public static class User
    {
        public static boolean authenticate(String username, String password) {

            Map<String, String> authenticationData = new HashMap<String, String>();
            authenticationData.put("polyakov", "the-best");
            authenticationData.put("rojkovsky", "rock-n-roll");

            String correctPassword = authenticationData.get(username);
            if (correctPassword != null && correctPassword.equals(password)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static class Login {
        
        public String username;
        public String password;

        public String validate() {
            if (User.authenticate(username, password) == false) {
                return Http.Context.current().messages().at("login.error.message");
            }
            return null;
        }
    }

    public Result login() {

        return ok (
            views.html.loginForm.render(formFactory.form(Login.class))
        );
    }

    public Result authenticate() {

        Form<Login> form = formFactory.form(Login.class).bindFromRequest();
        if (form.hasErrors()) {
            return ok(views.html.loginForm.render(form));
        } else {
            session().clear();
            session("username", form.get().username);
            return redirect(
                routes.HomeController.index()
            );
        } 
    }
}