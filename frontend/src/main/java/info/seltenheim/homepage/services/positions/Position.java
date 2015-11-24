package info.seltenheim.homepage.services.positions;

import info.seltenheim.homepage.services.DateRangeModel;
import info.seltenheim.homepage.services.PersistentModel;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.mvc.Controller;

public abstract class Position implements PersistentModel {
    private String id;

    private String fromDate;
    private String toDate;
    private String place;
    private Map<String, String> titleMap;
    private String website;

    public Position() {
        titleMap = new HashMap<String, String>();
    }

    public Position(String fromDate, String toDate, String place, Map<String, String> titleMap, String website) {
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Map<String, String> getTitleMap() {
        return titleMap;
    }

    public void setTitleMap(Map<String, String> titleMap) {
        this.titleMap = titleMap;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
