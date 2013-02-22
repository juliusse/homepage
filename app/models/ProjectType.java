package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;


@Entity
public class ProjectType extends Model{
    private static final Model.Finder<Long,ProjectType> find = new Model.Finder<Long,ProjectType>(Long.class, ProjectType.class);
    
    @Id
    private Long id;
    private String name;
    
    @ManyToMany(mappedBy="typeOf")
    private List<Project> projects; 
    
    public ProjectType() {
        
    }
    
    public ProjectType(String name) {
        super();
        this.name = name;
    }
    
    public static ProjectType findByName(String name) {
        return find.where().ieq("name", name).findUnique();
    }
    
    public static ProjectType application() {
        ProjectType type = findByName("Application");
        if(type == null) {
            type = new ProjectType("Application");
            type.save();
        }
        return type;
    }
    
    public static ProjectType web() {
        ProjectType type = findByName("Webapplication");
        if(type == null) {
            type = new ProjectType("Webapplication");
            type.save();
        }
        return type;
    }
    
    public static ProjectType game() {
        ProjectType type = findByName("Game");
        if(type == null) {
            type = new ProjectType("Game");
            type.save();
        }
        return type;
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
