package services.usertracking;

import java.io.IOException;

import info.seltenheim.play2.usertracking.usertrackingservice.UserTrackingService;
import info.seltenheim.play2.usertracking.usertrackingservice.UserTrackingServiceConsole;
import play.Logger;
import play.mvc.Http.Context;

public class UserTrackingServiceMongo extends UserTrackingServiceConsole implements UserTrackingService {

    @Override
    public void track(Context context, String controller) {
        this.track(context, controller, "");
    }

    @Override
    public void track(Context context, String controller, String action) {
        super.track(context, controller,action);
        final String session = session(context);
        final String userAgentString = context.request().getHeader("user-agent");
        
        try {
            DatabaseActions.saveTrackingEntry(session, userAgentString, controller, action);
        } catch (IOException e) {
            Logger.error("Problem saving tracking entry! ", e);
        }
    }

}
