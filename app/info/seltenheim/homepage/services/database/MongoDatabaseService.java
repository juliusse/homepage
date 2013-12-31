package info.seltenheim.homepage.services.database;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.getStringList;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.presentFields;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.projects;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.queryById;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.skillGroups;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.users;

import info.seltenheim.homepage.services.database.Project.ProjectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@Profile("MongoDb")
@Component
public class MongoDatabaseService implements DatabaseService {
    protected static final DateTimeFormatter persistenceDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    public static final BasicDBObject DEFAULT_PRESENT_FIELDS_PROJECT = presentFields("titleMap", "descriptionMap", "developmentStart", "developmentEnd", "displayOnStart", "projectTypes",
            "technologies", "mainImagePath", "filePath");
    public static final BasicDBObject DEFAULT_PRESENT_FIELDS_USER = presentFields("name", "password");
    public static final BasicDBObject DEFAULT_PRESENT_FIELDS_SKILL_GROUP = presentFields("nameMap", "skills", "displayRank");

    @Override
    public User upsertUser(User user) throws IOException {
        try {
            final BasicDBObject document = doc("name", user.getUsername()).append("password", user.getPassword());
            String id = user.getId();
            if (id == null) { // insert
                users().insert(document);
                id = document.get("_id").toString();
            } else {
                projects().update(queryById(id), document);
            }

            return findUserByName(user.getUsername());

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public User findUserByName(String name) throws IOException {
        try {
            final BasicDBObject query = doc("name", name);
            final BasicDBObject userBson = (BasicDBObject) users().findOne(query, DEFAULT_PRESENT_FIELDS_USER);
            final String username = userBson.getString("name");
            final String password = userBson.getString("password");
            return new User(username, password);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Project findProjectById(String projectId) throws IOException {
        try {
            final BasicDBObject query = queryById(projectId);
            final BasicDBObject projectBson = (BasicDBObject) projects().findOne(query, DEFAULT_PRESENT_FIELDS_PROJECT);
            return convertToProject(projectBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Project> findAllProjects() throws IOException {
        try {
            final List<Project> projects = new ArrayList<Project>();
            DBCursor cursor = null;
            try {
                cursor = projects().find();

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        projects.add(convertToProject((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return projects;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Project> findProjectsForStartPage() throws IOException {
        try {
            final List<Project> projects = new ArrayList<Project>();
            final BasicDBObject query = doc("displayOnStart", true);
            DBCursor cursor = null;
            try {
                cursor = projects().find(query, DEFAULT_PRESENT_FIELDS_PROJECT);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        projects.add(convertToProject((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return projects;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Project> findProjectsForCurrent() throws IOException {
        try {
            final List<Project> projects = new ArrayList<Project>();
            final BasicDBObject query = doc("developmentEnd", null);
            DBCursor cursor = null;
            try {
                cursor = projects().find(query, DEFAULT_PRESENT_FIELDS_PROJECT);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        projects.add(convertToProject((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return projects;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Project> findProjectsOfType(ProjectType type) throws IOException {
        try {
            final List<Project> projects = new ArrayList<Project>();
            final BasicDBObject query = doc("projectTypes", type.toString());
            DBCursor cursor = null;
            try {
                cursor = projects().find(query, DEFAULT_PRESENT_FIELDS_PROJECT);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        projects.add(convertToProject((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return projects;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Project upsertProject(Project project) throws IOException {
        try {
            final BasicDBObject document = convert(project);
            String id = project.getId();
            if (id == null) { // insert
                projects().insert(document);
                id = document.get("_id").toString();

            } else {
                projects().update(queryById(id), document);
            }

            return findProjectById(id);

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public SkillGroup findSkillGroupById(String skillGroupId) throws IOException {
        try {
            final BasicDBObject query = queryById(skillGroupId);
            final BasicDBObject skillGroupBson = (BasicDBObject) skillGroups().findOne(query, DEFAULT_PRESENT_FIELDS_SKILL_GROUP);
            return convertToSkillGroup(skillGroupBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<SkillGroup> findAllSkillGroups() throws IOException {
        try {
            final List<SkillGroup> skillGroups = new ArrayList<SkillGroup>();
            DBCursor cursor = null;
            try {
                cursor = skillGroups().find().sort(doc("displayRank", 1));

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        skillGroups.add(convertToSkillGroup((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return skillGroups;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public SkillGroup upsertSkillGroup(SkillGroup skillGroup) throws IOException {
        try {

            String id = skillGroup.getId();
            if (id == null) { // insert
                final Integer displayRank = (int) (skillGroups().count() + 1);
                skillGroup.setDisplayRank(displayRank);
                final BasicDBObject document = convert(skillGroup);
                skillGroups().insert(document);
                id = document.get("_id").toString();

            } else {
                final BasicDBObject document = convert(skillGroup);
                skillGroups().update(queryById(id), document);
            }

            return findSkillGroupById(id);

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public SkillGroup addSkillToGroup(String skillGroupId, Skill skill) throws IOException {
        final SkillGroup skillGroup = findSkillGroupById(skillGroupId);
        skillGroup.addSkill(skill);
        return upsertSkillGroup(skillGroup);
    }

    private BasicDBObject convert(SkillGroup skillGroup) {

        return doc("displayRank", skillGroup.getDisplayRank()).append("skills", convertSkillsListToSkillsJsonStrings(skillGroup.getSkills())).append("nameMap", skillGroup.getNameMap());
    }

    private SkillGroup convertToSkillGroup(BasicDBObject bson) {
        SkillGroup result = null;
        if (bson != null) {
            final String idFromBson = bson.get("_id").toString();
            final Integer displayRank = bson.getInt("displayRank");

            final List<String> skillStrings = getStringList(bson, "skills");

            @SuppressWarnings("unchecked")
            final Map<String, String> nameMap = (Map<String, String>) bson.get("nameMap");
            result = new SkillGroup(nameMap, convertSkillsJsonStringsToSkills(skillStrings));
            result.setId(idFromBson);
            result.setDisplayRank(displayRank);
        }
        return result;
    }

    private List<Skill> convertSkillsJsonStringsToSkills(List<String> skillsJson) {
        final List<Skill> skills = new ArrayList<Skill>();
        final ObjectMapper mapper = new ObjectMapper();
        for (String skillString : skillsJson) {
            try {
                skills.add(mapper.readValue(skillString, Skill.class));
            } catch (Exception e) {
                Logger.error("error parsing skill!", e);
            }
        }
        return skills;
    }

    private List<String> convertSkillsListToSkillsJsonStrings(List<Skill> skills) {
        final List<String> skillsJson = new ArrayList<String>();
        final ObjectMapper mapper = new ObjectMapper();
        for (Skill skill : skills) {
            try {
                skillsJson.add(mapper.writeValueAsString(skill));
            } catch (Exception e) {
                Logger.error("error parsing skill!", e);
            }
        }
        return skillsJson;
    }

    @SuppressWarnings("unchecked")
    private Project convertToProject(BasicDBObject bson) {
        Project result = null;
        if (bson != null) {
            final String idFromBson = bson.get("_id").toString();
            final boolean displayOnStartPage = Boolean.parseBoolean(bson.get("displayOnStart").toString());

            DateTime developmentStart = null;
            try {
                developmentStart = persistenceDateTimeFormatter.parseDateTime((bson.get("developmentStart").toString()));
            } catch (IllegalArgumentException e) {
                developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime((bson.get("developmentStart").toString()));
            }

            final Object devEndObj = bson.get("developmentEnd");
            DateTime developmentEnd = null;
            try {
                developmentEnd = devEndObj == null ? null : persistenceDateTimeFormatter.parseDateTime(devEndObj.toString());
            } catch (IllegalArgumentException e) {
                developmentEnd = Project.PROJECT_DATETIME_FORMAT.parseDateTime(devEndObj.toString());
            }

            final Object mainImagePathObj = bson.get("mainImagePath");
            final String mainImagePath = mainImagePathObj != null ? mainImagePathObj.toString() : "";

            final String filePath = bson.get("filePath") == null ? null : bson.get("filePath").toString();
            final List<ProjectType> typeOf = convertToProjectTypeList(getStringList(bson, "projectTypes"));
            final List<String> technologyList = getStringList(bson, "technologies");
            final Map<String, String> titleMap = (Map<String, String>) bson.get("titleMap");
            final Map<String, String> descriptionMap = (Map<String, String>) bson.get("descriptionMap");
            result = new Project(titleMap, descriptionMap, displayOnStartPage, technologyList, developmentStart, developmentEnd, mainImagePath, filePath, typeOf);
            result.setId(idFromBson);
        }
        return result;
    }

    private BasicDBObject convert(Project project) {
        final BasicDBObject document = doc("displayOnStart", project.getDisplayOnStartPage()).append("developmentStart", project.getDevelopmentStart().toString(persistenceDateTimeFormatter))
                .append("projectTypes", convertToStringList(project.getTypeOf())).append("filePath", project.getFilePath()).append("technologies", project.getTechnologies())
                .append("titleMap", project.getTitleMap()).append("descriptionMap", project.getDescriptionMap()).append("mainImagePath", project.getMainImagePath());

        Logger.debug("mainImagePath: " + project.getMainImagePath());
        if (project.getDevelopmentEnd() != null)
            document.append("developmentEnd", project.getDevelopmentEnd().toString(persistenceDateTimeFormatter));

        return document;
    }

    private List<ProjectType> convertToProjectTypeList(List<String> typeStrings) {
        final List<ProjectType> projectTypes = new ArrayList<Project.ProjectType>();
        for (String typeString : typeStrings) {
            projectTypes.add(ProjectType.valueOf(typeString));
        }

        return projectTypes;
    }

    private <T> List<String> convertToStringList(List<T> list) {
        final List<String> stringList = new ArrayList<String>();
        for (T type : list) {
            stringList.add(type.toString());
        }
        return stringList;
    }

}
