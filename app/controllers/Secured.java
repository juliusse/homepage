package controllers;


import models.User;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import static java.lang.String.format;

public class Secured extends Security.Authenticator {
    public static final String SESSION_KEY_EMAIL = "email";
    public static final String SESSION_KEY_USERID = "userId";
    public static final String SESSION_KEY_USERNAME = "username";
    
    @Override
    public String getUsername(Context ctx) {
        String result = null;
        final String userIdAsString = ctx.session().get(SESSION_KEY_USERID);
        final String userEmail = ctx.session().get(SESSION_KEY_EMAIL);
        if (userIdAsString != null) {
            final long userId = Long.parseLong(userIdAsString);
            if(userStillExists(userId, userEmail)) {
                result = userEmail;
            } else {
                Logger.debug(format("user with id=%s and email=%s does not exist.", userIdAsString, ctx.session().get(SESSION_KEY_EMAIL)));
                ctx.session().clear();
            }
        }
        if (result == null) {
            Logger.debug("is not authenticated");
        }
        return result;
    }

    private boolean userStillExists(long userId, String userMail) {
        User user = User.findById(userId);
        if(user != null) {
            if(user.getEmail().equals(userMail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.flash().put("error", "You need to authenticate.");
        return redirect(routes.Application.autoSelectLanguage());
    }
}