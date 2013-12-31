package info.seltenheim.homepage.services.database;

import info.seltenheim.homepage.controllers.Application;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class Education extends Position {

    private Map<String, String> degreeMap;
    private String score;

    public Education() {
        super();
        degreeMap = new HashMap<String, String>();
    }

    public Education(DateTime fromDate, DateTime toDate, String place, String website, String score, Map<String, String> titleMap, Map<String, String> degreeMap, String degreeNote) {
        super(fromDate, toDate, place, titleMap, website);

        this.degreeMap = degreeMap;
        this.score = score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDegree() {
        final String lang = Application.getSessionLang();
        if (lang.equals("de")) {
            return degreeMap.get("de");
        } else {
            return degreeMap.get("en");
        }
    }

    public String getDegree(String langKey) {
        return degreeMap.get(langKey);
    }

    public void setDegree(String lang, String degree) {
        this.degreeMap.put(lang, degree);
    }

    public Map<String, String> getDegreeMap() {
        return degreeMap;
    }

}
