package service.database;

import java.util.List;

import models.Project;
import models.User;

public interface DatabaseService {

    public abstract User upsertUser(User user);
    public abstract User findUserByName(String name);
    
    public abstract Project findProjectById(String projectId);

    public abstract List<Project> findAllProjects();

    public abstract List<Project> findProjectsForStartPage();

    public abstract List<Project> findProjectsForCurrent();

    public abstract List<Project> findProjectsOfType(String typeString);
    
    public abstract Project upsertProject(Project project);

}