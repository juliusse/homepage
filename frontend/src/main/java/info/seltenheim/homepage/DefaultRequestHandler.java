package info.seltenheim.homepage;

import play.Logger;
import play.http.DefaultHttpRequestHandler;
import play.i18n.Lang;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Method;

public class DefaultRequestHandler extends DefaultHttpRequestHandler {
    @Override
    public Action createAction(Http.Request request, Method method) {
        logRequest(request, method);

        // final String action = method.getName();
        // final String controller = method.getDeclaringClass().getSimpleName();

        return new Action.Simple() {

            @Override
            public F.Promise<Result> call(Http.Context ctx) throws Throwable {
                if (request.uri().length() > 3) {
                    final String langKey = request.uri().substring(1, 3);
                    final String currentLang = ctx.lang().language();
                    if (!langKey.equals(currentLang) && (langKey.equals("de") || langKey.equals("en"))) {
                        ctx.changeLang(langKey);
                        return F.Promise.pure(Controller.redirect(ctx.request().path()));
                    }
                }

                // TrackAsAction.call(ctx, controller, action);
                return delegate.call(ctx); 
            }
        };
    }

    @Override
    public Action wrapAction(Action action) {
        return super.wrapAction(action);
    }

    private void logRequest(Http.Request request, Method method) {
        if (Logger.isDebugEnabled() && !request.path().startsWith("/assets")) {
            StringBuilder sb = new StringBuilder(request.toString());
            sb.append(" ").append(method.getDeclaringClass().getCanonicalName());
            sb.append(".").append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int j = 0; j < params.length; j++) {
                sb.append(params[j].getCanonicalName().replace("java.lang.", ""));
                if (j < (params.length - 1))
                    sb.append(',');
            }
            sb.append(")");
            Logger.debug(sb.toString());
        }
    }
}
