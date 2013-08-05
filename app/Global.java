import info.seltenheim.play2.usertracking.actions.TrackUserAction;

import java.lang.reflect.Method;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.filesystem.FileSystemService;
import configuration.SpringConfiguration;


public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        initializeSpring();
        
        super.onStart(application);
        
    }
    
    private void initializeSpring() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        configuration.SpringConfiguration.initializeContext(context);
        Logger.debug("di worked: "+(SpringConfiguration.getBean(FileSystemService.class) != null));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Action onRequest(Http.Request request, Method method) {
        logRequest(request, method);

        final String action = method.getName();
        final String controller = method.getDeclaringClass().getSimpleName();

        return new Action.Simple() {

            @Override
            public Result call(Context ctx) throws Throwable {
                final Result result = delegate.call(ctx);
                TrackUserAction.call(ctx, controller, action);
                return result;
            }
        };
    }
    
    @Override
    public <A> A getControllerInstance(Class<A> clazz) throws Exception {
        A bean = SpringConfiguration.getBean(clazz);
        if (bean == null) {
            bean = super.getControllerInstance(clazz);
        }
        return bean;
    }
    
    private void logRequest(Http.Request request, Method method) {
        if(Logger.isDebugEnabled() && !request.path().startsWith("/assets")) {
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
