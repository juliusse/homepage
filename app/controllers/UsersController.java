package controllers;

import static controllers.secured.OnlyLoggedIn.SESSION_KEY_EMAIL;
import static controllers.secured.OnlyLoggedIn.SESSION_KEY_USERNAME;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import controllers.secured.OnlyLoggedIn;
import models.User;
import models.forms.CredentialsFormData;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.database.DatabaseService;

@Component
@Security.Authenticated(OnlyLoggedIn.class)
public class UsersController extends Controller {
    private static final Form<CredentialsFormData> credentialsForm = Form.form(CredentialsFormData.class);
    
    @Autowired
    private DatabaseService databaseService;
    
    public Result logout() {
        Logger.debug("logout " + session(SESSION_KEY_EMAIL));
        session().clear();
        return redirect(routes.Application.index(Application.getSessionLang()));
    }

    public User getLoggedInUser() {
        String username = session(SESSION_KEY_USERNAME);
        if(username != null) {
            return databaseService.findUserByName(username);
        } else
            return null;
    }
    
    public Result login(String langKey) throws JsonGenerationException, JsonMappingException, IOException {
        final Form<CredentialsFormData> filledForm = credentialsForm.bindFromRequest();

        // Logger.debug(filledForm.toString());

        Result result;
        if (getLoggedInUser() != null) {
            result = redirect(routes.Application.index(langKey));

        } else if (filledForm.hasErrors()) {
            result = badRequest(views.html.login.render());

        } else {
            final CredentialsFormData credentials = filledForm.get();
            User user = authenticate(credentials.getUsername(), credentials.getPassword());
            final boolean authenticationSuccessful = user != null;
            if (authenticationSuccessful) {
                session(SESSION_KEY_EMAIL, user.getEmail());
                session(SESSION_KEY_USERNAME, user.getUsername());

                result = redirect(routes.Application.index(langKey));
            } else {
                filledForm.reject("The credentials doesn't match any user.");
                Logger.debug(credentials.getUsername() + " is unauthorized");
                result = unauthorized(views.html.login.render());
            }
        }
        return result;
    }

    public User authenticate(String username, String password) {
        final User user = databaseService.findUserByName(username);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
    
    public static boolean isLoggedIn() {
        String username = session(SESSION_KEY_USERNAME);
        return username != null;
    }
}
