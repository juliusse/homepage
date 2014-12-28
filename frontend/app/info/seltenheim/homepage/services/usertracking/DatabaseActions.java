package info.seltenheim.homepage.services.usertracking;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.queryById;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.trackings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public class DatabaseActions {

    public static List<Tracking> findAllTrackings() throws IOException {
        try {
            final List<Tracking> trackings = new ArrayList<Tracking>();
            DBCursor cursor = null;
            try {
                cursor = trackings().find();

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        trackings.add(toTracking((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return trackings;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public static List<Tracking> findTrackingsWhereUserAgentContains(String uaContains) throws IOException {
        try {
            final BasicDBObject query = doc("uaString", doc("$regex", uaContains));
            final List<Tracking> trackings = new ArrayList<Tracking>();
            DBCursor cursor = null;
            try {
                cursor = trackings().find(query);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        trackings.add(toTracking((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return trackings;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public static int deleteTrackingsWhereUserAgentEquals(String userAgent) throws IOException {
        try {
            final BasicDBObject query = doc("uaString", userAgent);

            return trackings().remove(query).getN();

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public static Tracking saveTrackingEntry(String session, String userAgentString, String controller, String action) throws IOException {
        Tracking tracking = findTrackingBySession(session);
        if (tracking == null) {
            tracking = Tracking.create(session, userAgentString);
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

            return toTracking(trackingBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public static Tracking upsertTracking(Tracking tracking) throws IOException {
        try {
            final BasicDBObject document = doc("session", tracking.getSession()).append("visitedPages", convert(tracking.getVisitedPages())).append("uaString", tracking.getUserAgentString());
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

    private static Tracking toTracking(BasicDBObject trackingBson) {
        final String id = trackingBson.getString("_id");
        final String userAgentString = trackingBson.getString("uaString");
        final String session = trackingBson.getString("session");
        final List<VisitedPage> visitedPages = toVisitedPagesList((BasicDBList) trackingBson.get("visitedPages"));

        return new Tracking(id, session, userAgentString, visitedPages);
    }
}
