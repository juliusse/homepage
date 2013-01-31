package models;

import javax.persistence.Entity;
import javax.persistence.Id;


public class ProjectType {
    
    @Id
    private Long id;
    private String name;
    
    public ProjectType() {
        
    }
    
    public ProjectType(String name) {
        super();
        this.name = name;
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
    
    
}
