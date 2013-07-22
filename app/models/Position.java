package models;

import java.util.Random;

import org.joda.time.DateTime;

public abstract class Position {

    private final Long id;

    private DateTime fromDate;
    private DateTime toDate;
    private String place;
    private String title;
    private String website;

    public Position(DateTime fromDate, DateTime toDate, String place, String title, String website) {
        super();
        this.id = new Random().nextLong();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.place = place;
        this.title = title;
        this.website = website;
    }

    public Long getId() {
        return id;
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
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    
}
