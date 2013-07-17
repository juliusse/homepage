package services.database;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import services.database.Project.ProjectType;

@Component
public interface DatabaseService {

    // User methods
    User upsertUser(User user) throws IOException;

    User findUserByName(String name) throws IOException;

    // Project methods
    Project findProjectById(String projectId) throws IOException;

    List<Project> findAllProjects() throws IOException;

    List<Project> findProjectsForStartPage() throws IOException;

    List<Project> findProjectsForCurrent() throws IOException;

    List<Project> findProjectsOfType(ProjectType type) throws IOException;

    Project upsertProject(Project project) throws IOException;

    // SkillGroup methods
    SkillGroup findSkillGroupById(String skillGroupId) throws IOException;

    List<SkillGroup> findAllSkillGroups() throws IOException;

    SkillGroup upsertSkillGroup(SkillGroup skillGroup) throws IOException;

    SkillGroup addSkillToGroup(String skillGroupId, Skill skill) throws IOException;

}