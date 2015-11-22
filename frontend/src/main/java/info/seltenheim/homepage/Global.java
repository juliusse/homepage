package info.seltenheim.homepage;

import java.lang.reflect.Method;

import play.GlobalSettings;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;

public class Global extends GlobalSettings {

    @SuppressWarnings("rawtypes")
    @Override
    public Action onRequest(final Http.Request request, Method method) {
        logRequest(request, method);

        // final String action = method.getName();
        // final String controller = method.getDeclaringClass().getSimpleName();

        return new Action.Simple() {

            @Override
            public Promise<Result> call(Context ctx) throws Throwable {
                if (request.uri().length() > 3) {
                    final String langKey = request.uri().substring(1, 3);
                    if (langKey.equals("de") || langKey.equals("en")) {
                        Controller.changeLang(langKey);
                    }
                }

                final Promise<Result> result = delegate.call(ctx);
                // TrackAsAction.call(ctx, controller, action);
                return result;
            }
        };
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
