package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Skill extends Model{

    @Id
    private Long id;
    
    private String name;
    private double knowledge;
    
    public Skill() {
        
    }

    public Skill(String name, double knowledge) {
        super();
        this.name = name;
        this.knowledge = knowledge;
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

    public double getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(double knowledge) {
        this.knowledge = knowledge;
    }
        
}
