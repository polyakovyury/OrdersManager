package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import java.util.Map;
import java.util.HashMap;


@Entity 
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;
    
    public String email;

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