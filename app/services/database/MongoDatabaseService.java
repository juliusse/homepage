package services.database;

import static plugins.mongo.MongoPlugin.doc;
import static plugins.mongo.MongoPlugin.getStringList;
import static plugins.mongo.MongoPlugin.positions;
import static plugins.mongo.MongoPlugin.presentFields;
import static plugins.mongo.MongoPlugin.projects;
import static plugins.mongo.MongoPlugin.queryById;
import static plugins.mongo.MongoPlugin.skillGroups;
import static plugins.mongo.MongoPlugin.users;

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
import services.database.Project.ProjectType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@Profile("MongoDb")
@Component
public class MongoDatabaseService implements DatabaseService {
    private static final DateTimeFormatter persistenceDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Position> T upsertPosition(T position) throws IOException {
        final boolean isEmployment = position instanceof Employment;
        try {
            String id = position.getId();
            BasicDBObject document = null;
            if(isEmployment) {
                document = convert((Employment)position);
            } else {
                document = convert((Education)position);
            }
            
            if (id == null) { // insert
                positions().insert(document);
                id = document.get("_id").toString();
            } else {
                positions().update(queryById(id), document);
            }

            if(isEmployment) {
                return (T) findEmploymentById(id);
            } else {
                return (T) findEducationById(id);
            }

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Position findPositionById(String positionId) throws IOException {
        try {
            final BasicDBObject query = queryById(positionId);
            final BasicDBObject positionJson = (BasicDBObject) positions().findOne(query);
            return convertToPosition(positionJson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public List<Position> findCurrentPositions() throws IOException {
        try {
            final List<Position> positions = new ArrayList<Position>();
            final BasicDBObject query = doc("toDate", null);
            DBCursor cursor = null;
            try {
                cursor = positions().find(query);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        positions.add(convertToPosition((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return positions;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Employment> findAllEmployments() throws IOException {
        try {
            final List<Employment> employments = new ArrayList<Employment>();
            final BasicDBObject query = doc("type", "employment");
            DBCursor cursor = null;
            try {
                cursor = positions().find(query);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        employments.add(convertToEmployment((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return employments;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Employment findEmploymentById(String employmentId) throws IOException {
        try {
            final BasicDBObject query = queryById(employmentId);
            final BasicDBObject employmentBson = (BasicDBObject) positions().findOne(query);
            Logger.debug(employmentBson.toString());
            return convertToEmployment(employmentBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public List<Education> findAllEducations() throws IOException {
        try {
            final List<Education> educations = new ArrayList<Education>();
            final BasicDBObject query = doc("type", "education");
            DBCursor cursor = null;
            try {
                cursor = positions().find(query).sort(doc("fromDate", -1));

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        educations.add(convertToEducation((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return educations;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Education findEducationById(String educationId) throws IOException {
        try {
            final BasicDBObject query = queryById(educationId);
            final BasicDBObject employmentBson = (BasicDBObject) positions().findOne(query);
            return convertToEducation(employmentBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
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
            final DateTime developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime((bson.get("developmentStart").toString()));

            final Object devEndObj = bson.get("developmentEnd");
            final DateTime developmentEnd = devEndObj == null ? null : Project.PROJECT_DATETIME_FORMAT.parseDateTime((devEndObj.toString()));

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

    private BasicDBObject convert(Position position) {
        Logger.debug(position.getFromDate().toString());
        final BasicDBObject document = 
                doc("type", position instanceof Employment ? "employment" : "education")
                .append("fromDate", position.getFromDate().toString(persistenceDateTimeFormatter))
                .append("place", position.getPlace())
                .append("website", position.getWebsite())
                .append("titleMap", position.getTitleMap());
        
        if(position.getToDate() != null) {
            document.append("toDate", position.getToDate().toString(persistenceDateTimeFormatter));
        }
        
        return document;
    }

    @SuppressWarnings("unchecked")
    private Position convertToPosition(BasicDBObject bson) {
        Position result = null;
        if (bson != null) {
            final String idFromBson = bson.get("_id").toString();
            final String type = bson.getString("type");
            final String place = bson.getString("place");
            final DateTime fromDate = persistenceDateTimeFormatter.parseDateTime((bson.getString("fromDate")));
            
            final String toDateString = bson.getString("toDate");
            final DateTime toDate = toDateString == null ? null : persistenceDateTimeFormatter.parseDateTime(toDateString);
            final String website = bson.getString("website");
            final Map<String,String> titleMap = (Map<String,String>) bson.get("titleMap");
            if (type.equals("employment")) {
                final Map<String,List<String>> tasksMap = (Map<String,List<String>>)bson.get("tasksMap");
                result = new Employment(fromDate, toDate, place, titleMap, website, tasksMap);
            } else if (type.equals("education")) {
                final Map<String,String> degreeMap = (Map<String,String>)bson.get("degreeMap");
                final String degreeNote = bson.getString("degreeNote");
                final String score = bson.getString("score");

                result = new Education(fromDate, toDate, place, website, score, titleMap, degreeMap, degreeNote);
            }

            result.setId(idFromBson);
        }
        return result;
    }

    private Employment convertToEmployment(BasicDBObject bson) {
        return (Employment) convertToPosition(bson);
    }

    private Education convertToEducation(BasicDBObject bson) {
        return (Education) convertToPosition(bson);
    }

    private BasicDBObject convert(Employment employment) {
        return convert((Position) employment).append("tasksMap", employment.getTasksMap());
    }

    private BasicDBObject convert(Education education) {
        return convert((Position) education).append("degreeMap", education.getDegreeMap())
                .append("score", education.getScore());
    }

}
