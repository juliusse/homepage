package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;


public class Education extends Model{
    
    @Id
    private Long id;
    
    private String fromYear;
    private String toYear;
    private String place;
    private String website;
    private String score;
    private String scale;
    private String degree;
    private String degreeNote;
    
    public Education() {
        
    }

    public Education(String fromYear, String toYear, String place, String website, String score, String scale, String degree, String degreeNote) {
        super();
        this.fromYear = fromYear;
        this.toYear = toYear;
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

    public String getFrom() {
        return fromYear;
    }

    public void setFrom(String from) {
        this.fromYear = from;
    }

    public String getTo() {
        return toYear;
    }

    public void setTo(String to) {
        this.toYear = to;
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
