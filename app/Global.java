import java.lang.reflect.Method;

import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;


public class Global extends GlobalSettings {

    @Override
    public void onStart(Application arg0) {
        super.onStart(arg0);
        
//        
//        if (User.findAll().size() == 0) { //server restarted
//            populateDatabase();
//        }
    }
    
    private void populateDatabase() { 
        //new User("Julius", "mail@julius-seltenheim.com", "secret").save();
    }
    
    @Override
    public Action onRequest(Http.Request request, Method method) {
        logRequest(request, method);

        return super.onRequest(request, method);
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
