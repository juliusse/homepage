package models;

import org.joda.time.DateTime;



public class Education {

    private Long id;
    
    private DateTime fromDate;
    private DateTime toDate;
    private String place;
    private String website;
    private String score;
    private String scale;
    private String degree;
    private String degreeNote;
    
    public Education() {
        
    }

    public Education(DateTime fromDate, DateTime toDate, String place, String website, String score, String scale, String degree, String degreeNote) {
        super();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.place = place;
        this.website = website;
        this.score = score;
        this.scale = scale;
        this.degree = degree;
        this.degreeNote = degreeNote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDegreeNote() {
        return degreeNote;
    }

    public void setDegreeNote(String degreeNote) {
        this.degreeNote = degreeNote;
    }
    
    
}
