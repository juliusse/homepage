package services.database;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import services.database.Project.ProjectType;

@Component
public interface DatabaseService {

    User upsertUser(User user) throws IOException;
    User findUserByName(String name) throws IOException;
    
    Project findProjectById(String projectId) throws IOException;

    List<Project> findAllProjects() throws IOException;

    List<Project> findProjectsForStartPage() throws IOException;

    List<Project> findProjectsForCurrent() throws IOException;

    List<Project> findProjectsOfType(ProjectType type) throws IOException;
    
    Project upsertProject(Project project) throws IOException;

}