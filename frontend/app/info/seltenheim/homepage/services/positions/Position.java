package info.seltenheim.homepage.services.positions;

import info.seltenheim.homepage.services.DateRangeModel;
import info.seltenheim.homepage.services.PersistentModel;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.mvc.Controller;

public abstract class Position implements PersistentModel, DateRangeModel {
    public static final DateTimeFormatter POSITION_DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM");

    private String id;

    private DateTime fromDate;
    private DateTime toDate;
    private String place;
    private Map<String, String> titleMap;
    private String website;

    public Position() {
        titleMap = new HashMap<String, String>();
    }

    public Position(DateTime fromDate, DateTime toDate, String place, Map<String, String> titleMap, String website) {
        super();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.place = place;
        this.titleMap = titleMap;
        this.website = website;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public DateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTitle() {
        final String lang = Controller.lang().language();
        if (lang.equals("de")) {
            return titleMap.get("de");
        } else {
            return titleMap.get("en");
        }
    }

    public String getTitle(String langKey) {
        return titleMap.get(langKey);
    }

    public void setTitle(String lang, String title) {
        this.titleMap.put(lang, title);
    }

    Map<String, String> getTitleMap() {
        return titleMap;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
