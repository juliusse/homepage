package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.controllers.secured.OnlyLoggedIn;
import info.seltenheim.homepage.services.usertracking.DatabaseActions;
import info.seltenheim.homepage.services.usertracking.Tracking;
import info.seltenheim.homepage.services.usertracking.VisitedPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Component
@Security.Authenticated(OnlyLoggedIn.class)
public class TrackingsController extends Controller {

    public Result index(String langKey) {
        Application.setSessionLang(langKey);
        return ok(info.seltenheim.homepage.views.html.tracking.index.render());
    }

    public Result uaByQuantityAndAppearance(String langKey) throws IOException {
        final List<Tracking> trackings = DatabaseActions.findAllTrackings();
        final Map<String, UserAgentQuantityAndLastAppearance> uaMap = new HashMap<String, TrackingsController.UserAgentQuantityAndLastAppearance>();

        for (Tracking tracking : trackings) {
            final String ua = tracking.getUserAgentString();
            if (!uaMap.containsKey(ua)) {
                uaMap.put(ua, new UserAgentQuantityAndLastAppearance(ua));
            }

            final UserAgentQuantityAndLastAppearance entry = uaMap.get(ua);
            entry.incrementQuantity();

            final List<VisitedPage> visitedPages = tracking.getVisitedPages();
            final VisitedPage lastVisited = visitedPages.get(visitedPages.size() - 1);
            final String lastSeen = lastVisited.getTimeString();

            try {
                final DateTime curLast = DateTime.parse(entry.getLastAppearance(), VisitedPage.DATETIME_FORMATTER);
                final DateTime possibleLast = DateTime.parse(lastSeen, VisitedPage.DATETIME_FORMATTER);
                if (possibleLast.isAfter(curLast))
                    entry.setLastAppearance(lastSeen);

            } catch (Exception e) {
                entry.setLastAppearance(lastSeen);
            }

        }

        final List<UserAgentQuantityAndLastAppearance> ranking = new ArrayList<TrackingsController.UserAgentQuantityAndLastAppearance>(uaMap.values());
        Collections.sort(ranking, new Comparator<UserAgentQuantityAndLastAppearance>() {
            @Override
            public int compare(UserAgentQuantityAndLastAppearance o1, UserAgentQuantityAndLastAppearance o2) {
                if (o1.getQuantity() > o2.getQuantity())
                    return -1;
                else if (o2.getQuantity() > o1.getQuantity())
                    return 1;

                return 0;
            }
        });
        return ok(info.seltenheim.homepage.views.html.tracking.uaByQuantity.render(ranking));
    }

    public Result listSessions(String langKey, String uaFilter, long minRequests) throws IOException {
        List<Tracking> trackings = null;
        if (uaFilter.isEmpty()) {
            trackings = DatabaseActions.findAllTrackings();
        } else {
            trackings = DatabaseActions.findTrackingsWhereUserAgentContains(uaFilter);
        }

        if (minRequests > 1) {
            final Iterator<Tracking> it = trackings.iterator();
            while (it.hasNext()) {
                final Tracking tracking = it.next();
                if (tracking.getVisitedPages().size() < minRequests) {
                    it.remove();
                }
            }
        }

        Collections.sort(trackings, new Comparator<Tracking>() {
            @Override
            public int compare(Tracking o1, Tracking o2) {
                final DateTime last1 = DateTime.parse(o1.getLastAppearance(), VisitedPage.DATETIME_FORMATTER);
                final DateTime last2 = DateTime.parse(o2.getLastAppearance(), VisitedPage.DATETIME_FORMATTER);

                if (last1.isAfter(last2)) {
                    return -1;
                } else if (last2.isAfter(last1)) {
                    return 1;
                } else
                    return 0;
            }
        });

        return ok(info.seltenheim.homepage.views.html.tracking.listSessions.render(trackings));
    }

    public Result deleteTrackingsByUserAgent(String langKey) throws IOException {
        final String uaFilter = request().body().asFormUrlEncoded().get("uaFilter")[0];
        Logger.debug("filter: " + uaFilter);
        final int affectedEntries = DatabaseActions.deleteTrackingsWhereUserAgentEquals(uaFilter);
        Logger.info("Deleted entries: " + affectedEntries);

        return redirect(routes.TrackingsController.uaByQuantityAndAppearance(langKey));
    }

    public static class UserAgentQuantityAndLastAppearance {
        private final String userAgent;
        private long quantity;
        private String lastAppearance;

        public UserAgentQuantityAndLastAppearance(String userAgent) {
            this.userAgent = userAgent;
            quantity = 0;
            lastAppearance = "not set";
        }

        public String getUserAgent() {
            return userAgent;
        }

        public long incrementQuantity() {
            return quantity++;
        }

        public String getLastAppearance() {
            return lastAppearance;
        }

        public void setLastAppearance(String lastAppearance) {
            this.lastAppearance = lastAppearance;
        }

        public long getQuantity() {
            return quantity;
        }

    }
}
