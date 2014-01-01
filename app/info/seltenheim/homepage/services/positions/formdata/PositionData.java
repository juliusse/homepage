package info.seltenheim.homepage.services.positions.formdata;

import info.seltenheim.homepage.services.positions.Position;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Constraints.Required;

public class PositionData {
    protected final static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    private String id = "-1";

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

    public PositionData() {

    }

    public PositionData(Position position) {
        id = position.getId();
        titleDe = position.getTitle("de");
        titleEn = position.getTitle("en");
        place = position.getPlace();
        website = position.getWebsite();

        fromDate = position.getFromDate().toString(dateTimeFormatter);
        if (position.getToDate() != null) {
            toDate = position.getToDate().toString(dateTimeFormatter);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getFromDate() {
        return fromDate;
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

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
