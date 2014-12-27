package info.seltenheim.homepage.services.skills;

import info.seltenheim.homepage.services.PersistentModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.mvc.Controller;

public class SkillGroup implements PersistentModel {

    private String id;
    private Map<String, String> nameMap;
    private List<Skill> skills;
    private Integer displayRank;

    public SkillGroup() {
        nameMap = new HashMap<String, String>();
        skills = new ArrayList<Skill>();
    }

    public SkillGroup(Map<String, String> nameMap, List<Skill> skills) {
        super();
        this.nameMap = nameMap;
        this.skills = skills;
        Collections.sort(skills, Collections.reverseOrder());
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        String lang = Controller.lang().language();
        if (nameMap.containsKey(lang))
            return nameMap.get(lang);
        else
            return "undefined";
    }

    public void setName(String langKey, String name) {
        nameMap.put(langKey, name);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public Integer getDisplayRank() {
        return displayRank;
    }

    void setDisplayRank(Integer displayRank) {
        this.displayRank = displayRank;
    }

    Map<String, String> getNameMap() {
        return nameMap;
    }

    void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

}
