package controllers;

import static controllers.Secured.SESSION_KEY_EMAIL;
import static controllers.Secured.SESSION_KEY_USERID;
import static java.lang.Long.parseLong;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class UsersController extends Controller {

    public static Result logout() {
        Logger.debug("logout " + session(SESSION_KEY_EMAIL));
        session().clear();
        return redirect(routes.Application.index(Application.getSessionLang()));
    }

    public static User getLoggedInUser() {
        String userId = session(SESSION_KEY_USERID);
        if(userId != null) {
            long id = parseLong(userId);
            return User.findById(id);
        } else
            return null;
    }

    /**
     * creates a user and all default settings
     *
     * @param email
     * @param password
     * @return
     */
    public static User createAndSaveUser(String email, String password) {
        String name = email.split("@")[0];
        String nameToUse = null;
        User userWithUsername = null;
        int count = 0;
        do {
            if (count == 0)
                nameToUse = name;
            else {
                nameToUse = name + count;
            }
            count++;
            userWithUsername = User.findByUsername(nameToUse);
        } while (userWithUsername != null);
        User user = new User(nameToUse, email, password);
        user.save();

        return user;
    }
}
