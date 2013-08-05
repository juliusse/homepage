package services.usertracking;

import static plugins.mongo.MongoPlugin.doc;
import static plugins.mongo.MongoPlugin.queryById;
import static plugins.mongo.MongoPlugin.trackings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;

public class DatabaseActions {

    public static Tracking saveTrackingEntry(String session, String controller, String action) throws IOException {
        Tracking tracking = findTrackingBySession(session);
        if (tracking == null) {
            tracking = Tracking.create(session);
        }
        tracking.addVisitedPage(new VisitedPage(controller, action, System.currentTimeMillis()));
        upsertTracking(tracking);
        return tracking;
    }

    public static Tracking findTrackingBySession(String session) throws IOException {
        try {
            final BasicDBObject query = doc("session", session);
            final BasicDBObject trackingBson = (BasicDBObject) trackings().findOne(query);
            if (trackingBson == null) {
                return null;
            }

            final String id = trackingBson.getString("_id");
            final List<VisitedPage> visitedPages = toVisitedPagesList((BasicDBList)trackingBson.get("visitedPages"));

            return new Tracking(id, session, visitedPages);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public static Tracking upsertTracking(Tracking tracking) throws IOException {
        try {
            final BasicDBObject document = doc("session", tracking.getSession()).append("visitedPages", convert(tracking.getVisitedPages()));
            String id = tracking.getId();
            if (id == null) { // insert
                trackings().insert(document);
                id = document.get("_id").toString();

            } else {
                trackings().update(queryById(id), document);
            }

            return findTrackingBySession(tracking.getSession());

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    private static BasicDBList convert(List<VisitedPage> visitedPages) {
        final BasicDBList basicDBList = new BasicDBList();
        for (VisitedPage vp : visitedPages) {
            basicDBList.add(doc("controller", vp.getController()).append("action", vp.getAction()).append("timeString", vp.getTimeString()));
        }

        return basicDBList;
    }

    private static List<VisitedPage> toVisitedPagesList(BasicDBList bson) {
        final List<VisitedPage> visitedPages = new ArrayList<VisitedPage>();
        for (Object obj : bson) {
            final BasicDBObject dbObj = (BasicDBObject) obj;
            final String controller = dbObj.getString("controller");
            final String action = dbObj.getString("action");
            final String timeString = dbObj.getString("timeString");
            visitedPages.add(new VisitedPage(controller, action, timeString));

        }
        
        return visitedPages;
    }

}
