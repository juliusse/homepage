package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.db.ebean.Model;


public class SkillGroup extends Model{

    @Id
    private Long id;
    private String name;
    @OneToMany
    private List<Skill> skills;
    
    public SkillGroup() {
        
    }

    public SkillGroup(String name, List<Skill> skills) {
        super();
        this.name = name;
        this.skills = skills;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}