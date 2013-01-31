package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

import play.db.ebean.Model;


public class Employment extends Model {

    @Id
    private Long id;
    
    private DateTime from;
    private DateTime to;
    private String place;
    private String title;
    private String website;
    private List<String> tasks;
    
    public Employment() {
        
    }

    public Employment(DateTime from, DateTime to, String place, String title, String website, List<String> tasks) {
        super();
        this.from = from;
        this.to = to;
        this.place = place;
        this.title = title;
        this.website = website;
        this.tasks = tasks;
    }
    
    public Employment(DateTime from, DateTime to, String place, String title, String website, String[] tasks) {
        super();
        this.from = from;
        this.to = to;
        this.place = place;
        this.title = title;
        this.website = website;
        this.tasks = Arrays.asList(tasks);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
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

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
