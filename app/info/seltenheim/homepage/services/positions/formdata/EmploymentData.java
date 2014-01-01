package info.seltenheim.homepage.services.positions.formdata;

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

    public List<String> getTechnologiesList() {
        return Arrays.asList(technologies.split("\n"));
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

}
