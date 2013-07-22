package models.forms;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Constraints.Required;

public class UpsertPositionData {
    protected final static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Required
    private String titleDe;
    @Required
    private String titleEn;
    @Required
    private String place;
    private String website;

    @Required
    private String fromDate;
    private String toDate;

    public String getTitleDe() {
        return titleDe;
    }

    public void setTitleDe(String titleDe) {
        this.titleDe = titleDe;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public DateTime getFromDateObject() {
        return dateTimeFormatter.parseDateTime(fromDate);
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDateObject() {
        return toDate.isEmpty() ? null : dateTimeFormatter.parseDateTime(toDate);
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
