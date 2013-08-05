package services.usertracking;

import org.joda.time.DateTime;

public class VisitedPage {
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    
    
    private final String controller;
    private final String action;
    private final String timeString;

    public VisitedPage(String controller, String action, long currentMillis) {
        super();
        this.controller = controller;
        this.action = action;
        this.timeString = new DateTime(currentMillis).toString(DATETIME_FORMAT);
    }

    public VisitedPage(String controller, String action, String timeString) {
        super();
        this.controller = controller;
        this.action = action;
        this.timeString = timeString;
    }

    public String getController() {
        return controller;
    }

    public String getAction() {
        return action;
    }

    public String getTimeString() {
        return timeString;
    }

}
