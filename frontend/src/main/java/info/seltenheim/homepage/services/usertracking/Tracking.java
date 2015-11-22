package info.seltenheim.homepage.services.usertracking;

import java.util.ArrayList;
import java.util.List;

public class Tracking {
    private String id;
    private String session;
    private String userAgentString;

    private List<VisitedPage> visitedPages;

    Tracking(String id, String session, String userAgentString, List<VisitedPage> visited) {
        super();
        this.id = id;
        this.session = session;
        this.visitedPages = visited;
        this.userAgentString = userAgentString;
    }
    
    public static Tracking create(String session, String userAgentString) {
        return new Tracking(null,session, userAgentString, new ArrayList<VisitedPage>());
    }
    
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getSession() {
        return session;
    }

    void setSession(String session) {
        this.session = session;
    }

    public List<VisitedPage> getVisitedPages() {
        return visitedPages;
    }

    void setVisitedPages(List<VisitedPage> visited) {
        this.visitedPages = visited;
    }
    
    public void addVisitedPage(VisitedPage visitedPage) {
        this.visitedPages.add(visitedPage);
    }
    
    public String getUserAgentString() {
        return userAgentString;
    }
    
    public String getLastAppearance() {
        return this.visitedPages.get(this.visitedPages.size()-1).getTimeString();
    }

}
