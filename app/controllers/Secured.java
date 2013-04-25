package controllers;


import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {
    public static final String SESSION_KEY_EMAIL = "email";
    //public static final String SESSION_KEY_USERID = "userId";
    public static final String SESSION_KEY_USERNAME = "username";

    @Override
    public String getUsername(Context ctx) {
        final String username = ctx.session().get(SESSION_KEY_USERNAME);
        if (username != null) {
            return username;
        } else {
            Logger.debug("is not authenticated");
            return null;
        }
    }

    //    private boolean userStillExists(String userMail) {
    //        User user = User.findByEmail(userMail);
    //        if(user != null) {
    //            if(user.getEmail().equals(userMail)) {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.flash().put("error", "You need to authenticate.");
        return redirect(routes.Application.autoSelectLanguage());
    }
}