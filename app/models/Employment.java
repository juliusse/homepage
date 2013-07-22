package models;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;


public class Employment extends Position {
    
    private List<String> tasks;


    public Employment(DateTime from, DateTime to, String place, String title, String website, List<String> tasks) {
        super(from,to,place,title,website);
        this.tasks = tasks;
    }
    
    public Employment(DateTime from, DateTime to, String place, String title, String website, String[] tasks) {
        super(from,to,place,title,website);
        this.tasks = Arrays.asList(tasks);
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
}
