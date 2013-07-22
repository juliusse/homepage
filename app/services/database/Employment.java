package services.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import controllers.Application;


public class Employment extends Position {
    
    private Map<String,List<String>> tasksMap;

    
    public Employment() {
        super();
        tasksMap = new HashMap<String, List<String>>();
    }

    public Employment(DateTime from, DateTime to, String place, Map<String, String> titleMap, String website, Map<String,List<String>> tasksMap) {
        super(from,to,place,titleMap,website);
        this.tasksMap = tasksMap;
    }

    public List<String> getTasks() {
        final String lang = Application.getSessionLang();
        if(lang.equals("de")) {
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
        for(String s : tasksMap.get(langKey)) {
            result += s+"\n";
        }
        return result;
    }
    
    public void setTasks(String lang, List<String> tasks) {
        tasksMap.put(lang, tasks);
    }
    
    Map<String, List<String>> getTasksMap() {
        return tasksMap;
    }
}
