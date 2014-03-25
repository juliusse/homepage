package info.seltenheim.homepage.services.usertracking;

import info.seltenheim.play2.plugins.usertracking.usertrackingservice.UserTrackingService;
import info.seltenheim.play2.plugins.usertracking.usertrackingservice.UserTrackingServiceConsole;

import java.io.IOException;

import play.Logger;
import play.mvc.Http.Context;

public class UserTrackingServiceMongo extends UserTrackingServiceConsole implements UserTrackingService {

    @Override
    public void track(Context context, String controller) {
        this.track(context, controller, "");
    }

    @Override
    public void track(Context context, String controller, String action) {
        final String session = session(context);
        super.track(context, controller, action);
        
        final String userAgentString = context.request().getHeader("user-agent");

        try {
            DatabaseActions.saveTrackingEntry(session, userAgentString, controller, action);
        } catch (IOException e) {
            Logger.error("Problem saving tracking entry! ", e);
        }
    }

}
