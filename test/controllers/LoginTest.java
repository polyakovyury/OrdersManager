import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static play.api.test.CSRFTokenHelper.addCSRFToken;
import static play.test.Helpers.*;

// Use FixMethodOrder to run the tests sequentially
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest extends WithApplication {

    @Test
    public void loginSuccess() {
        Result result = route(app, addCSRFToken(fakeRequest().uri(controllers.routes.AuthenticationController.login().url())));
        assertThat(result.status()).isEqualTo(OK);

        Map<String, String> data = new HashMap<>();
        data.put("username", "polyakov");
        data.put("password", "the-best");

        String loginUrl = controllers.routes.AuthenticationController.login().url();
        result = route(app, addCSRFToken(fakeRequest().bodyForm(data).method("POST").uri(loginUrl)));

        assertThat(result.status()).isEqualTo(OK);
    }

    @Test
    public void loginFailed() {
        Result result = route(app, addCSRFToken(fakeRequest().uri(controllers.routes.AuthenticationController.login().url())));
        assertThat(result.status()).isEqualTo(OK);

        Map<String, String> data = new HashMap<>();
        data.put("username", "polyakov");
        data.put("password", "the-best");

        String loginUrl = controllers.routes.AuthenticationController.login().url();
        result = route(app, addCSRFToken(fakeRequest().bodyForm(data).method("POST").uri(loginUrl)));

        assertThat(result.status()).isEqualTo(BAD_REQUEST);
    }

}
