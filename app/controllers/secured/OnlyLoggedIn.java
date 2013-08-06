package controllers.secured;


import controllers.routes;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class OnlyLoggedIn extends Security.Authenticator {
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

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.flash().put("error", "You need to authenticate.");
        return redirect(routes.Application.indexWithLanguageAutoSelect());
    }
}