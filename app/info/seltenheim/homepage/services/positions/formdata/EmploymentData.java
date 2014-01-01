package info.seltenheim.homepage.services.positions.formdata;

import info.seltenheim.homepage.services.positions.Employment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.data.validation.Constraints.Required;

/**
 * @author Julius
 * 
 */
public class EmploymentData extends PositionData {

    @Required
    private String tasksDe;
    @Required
    private String tasksEn;

    private String technologies = "";

    public EmploymentData() {
        super();
    }

    public EmploymentData(Employment employment) {
        super(employment);

        tasksDe = employment.getTasksAsString("de");
        tasksEn = employment.getTasksAsString("en");

        technologies = employment.getTechnologiesAsString();
    }

    public List<String> getTasksDeList() {
        return Arrays.asList(tasksDe.split("\n"));
    }

    public String getTasksDe() {
        return tasksDe;
    }

    public void setTasksDe(String tasksDe) {
        this.tasksDe = tasksDe;
    }

    public List<String> getTasksEnList() {
        return Arrays.asList(tasksEn.split("\n"));
    }

    public String getTasksEn() {
        return tasksEn;
    }

    public void setTasksEn(String tasksEn) {
        this.tasksEn = tasksEn;
    }

    public List<String> getTechnologiesList() {
        if(technologies.trim().isEmpty()) {
            return new ArrayList<String>();
        }
        
        return Arrays.asList(technologies.split("\n"));
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

}
