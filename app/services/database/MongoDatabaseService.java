package services.database;

import static plugins.mongo.MongoPlugin.doc;
import static plugins.mongo.MongoPlugin.getStringList;
import static plugins.mongo.MongoPlugin.presentFields;
import static plugins.mongo.MongoPlugin.projects;
import static plugins.mongo.MongoPlugin.queryById;
import static plugins.mongo.MongoPlugin.users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
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

    public static final BasicDBObject DEFAULT_PRESENT_FIELDS_PROJECT = presentFields("titleMap", "descriptionMap", "developmentStart", "developmentEnd", "displayOnStart", "projectTypes",
            "technologies", "mainImagePath", "filePath");
    public static final BasicDBObject DEFAULT_PRESENT_FIELDS_USER = presentFields("name", "password");

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
            return convertBasicDBObjectToProject(projectBson);
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
                        projects.add(convertBasicDBObjectToProject((BasicDBObject) dbObject));
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
                        projects.add(convertBasicDBObjectToProject((BasicDBObject) dbObject));
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
                        projects.add(convertBasicDBObjectToProject((BasicDBObject) dbObject));
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
                        projects.add(convertBasicDBObjectToProject((BasicDBObject) dbObject));
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
            final BasicDBObject document = convertProjectToBasicDBObject(project);
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

    @SuppressWarnings("unchecked")
    private Project convertBasicDBObjectToProject(BasicDBObject bson) {
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

    private BasicDBObject convertProjectToBasicDBObject(Project project) {
        final BasicDBObject document = doc("displayOnStart", project.getDisplayOnStartPage())
                .append("developmentStart", project.getDevelopmentStart().toString(Project.PROJECT_DATETIME_FORMAT))
                .append("projectTypes", convertToStringList(project.getTypeOf())).append("filePath", project.getFilePath())
                .append("technologies", project.getTechnologies())
                .append("titleMap", project.getTitleMap())
                .append("descriptionMap", project.getDescriptionMap())
                .append("mainImagePath", project.getMainImagePath());

        Logger.debug("mainImagePath: "+project.getMainImagePath());
        if (project.getDevelopmentEnd() != null)
            document.append("developmentEnd", project.getDevelopmentEnd().toString(Project.PROJECT_DATETIME_FORMAT));

        return document;
    }

    private List<ProjectType> convertToProjectTypeList(List<String> typeStrings) {
        final List<ProjectType> projectTypes = new ArrayList<Project.ProjectType>();
        for (String typeString : typeStrings) {
            projectTypes.add(ProjectType.valueOf(typeString));
        }

        return projectTypes;
    }

    private List<String> convertToStringList(List<ProjectType> projectTypes) {
        final List<String> typeStringList = new ArrayList<String>();
        for (ProjectType type : projectTypes) {
            typeStringList.add(type.toString());
        }
        return typeStringList;
    }
}
