package models.forms;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.data.validation.Constraints.Required;

public class UpsertEmploymentData extends UpsertPositionData {
    
    
    

    
    @Required
    private String tasksDe;
    @Required
    private String tasksEn;
    
    public List<String> getTasksDeList() {
        return Arrays.asList(tasksDe.split("\n"));
    }
    public void setTasksDe(String tasksDe) {
        this.tasksDe = tasksDe;
    }
    public List<String> getTasksEnList() {
        return Arrays.asList(tasksEn.split("\n"));
    }
    public void setTasksEn(String tasksEn) {
        this.tasksEn = tasksEn;
    }
}
