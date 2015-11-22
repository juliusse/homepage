package info.seltenheim.homepage.services.projects;

import info.seltenheim.homepage.services.projects.Project.ProjectType;

import java.io.IOException;
import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(ProjectServiceMongoDb.class)
public interface ProjectsService {
 // Project methods
    Project findProjectById(String projectId) throws IOException;

    List<Project> findAllProjects() throws IOException;

    List<Project> findProjectsForStartPage() throws IOException;

    List<Project> findProjectsForCurrent() throws IOException;

    List<Project> findProjectsOfType(ProjectType type) throws IOException;

    Project upsertProject(Project project) throws IOException;
}
