package info.seltenheim.homepage.services.positions;

import info.seltenheim.homepage.services.PersistentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import play.mvc.Controller;

public class Employment extends Position implements PersistentModel {

    private Map<String, List<String>> tasksMap;
    private List<String> technologies;

    public Employment() {
        super();
        tasksMap = new HashMap<String, List<String>>();
        technologies = new ArrayList<String>();
    }

    public Employment(DateTime fromDate, DateTime toDate, String place, Map<String, String> titleMap, String website, Map<String, List<String>> tasksMap, List<String> technologies) {
        super(fromDate, toDate, place, titleMap, website);
        this.tasksMap = tasksMap;
        this.technologies = technologies;
    }

    public List<String> getTasks() {
        final String lang = Controller.lang().language();
        if (lang.equals("de")) {
            return tasksMap.get("de");
        } else {
            return tasksMap.get("en");
        }
    }

    public List<String> getTasks(String langKey) {
        return tasksMap.get(langKey);
    }

    public String getTasksAsString(String langKey) {
        String result = "";
        for (String s : tasksMap.get(langKey)) {
            result += s + "\n";
        }
        return result;
    }

    public void setTasks(String lang, List<String> tasks) {
        tasksMap.put(lang, tasks);
    }

    Map<String, List<String>> getTasksMap() {
        return tasksMap;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public String getTechnologiesAsString() {
        String result = "";
        for (String s : technologies) {
            result += s + "\n";
        }
        return result;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

}
