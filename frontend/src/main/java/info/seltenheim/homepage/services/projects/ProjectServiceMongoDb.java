package info.seltenheim.homepage.services.projects;

import info.seltenheim.homepage.services.projects.Project.ProjectType;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class ProjectServiceMongoDb implements ProjectsService {
    private final ProjectMapper projectMapper;

    @Inject
    public ProjectServiceMongoDb(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public Project findProjectById(String projectId) throws IOException {
        return projectMapper.findById(projectId);
    }

    @Override
    public List<Project> findAllProjects() throws IOException {
        return projectMapper.findAll();
    }

    @Override
    public List<Project> findProjectsForStartPage() throws IOException {
        return projectMapper.find("displayOnStart", true);
    }

    @Override
    public List<Project> findProjectsForCurrent() throws IOException {
        return projectMapper.find("developmentEnd", null);
    }

    @Override
    public List<Project> findProjectsOfType(ProjectType type) throws IOException {
        return projectMapper.find("projectTypes", type.toString());
    }

    @Override
    public Project upsertProject(Project project) throws IOException {
        return projectMapper.upsert(project);
    }

}
