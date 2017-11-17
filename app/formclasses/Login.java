package formclasses;

import models.User;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Validate;
import play.data.validation.Constraints.Validatable;

import lombok.Getter;
import lombok.Setter;

@Validate
public class Login implements Validatable<String> {
    
    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private boolean remember; 

    public Login() {
        this.username = "";
        this.password = "";
        this.remember = false;
    }

    public Login(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    @Override
    public String validate() {
        if (User.authenticate(username, password)) {
            return null;
        }
        return "login.error.message";
    }
}