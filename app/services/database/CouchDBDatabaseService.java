package services.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.Logger;
import services.database.Project.ProjectType;

@Profile("CouchDb")
@Component
public class CouchDBDatabaseService implements DatabaseService {
    private static CouchDbConnector db;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static CouchDbConnector getDbConnection() {
        if (db == null) {
            try {
                HttpClient httpClient = new StdHttpClient.Builder().url("https://juliusse.iriscouch.com:6984").username("website").password("myS3cr3tBlub").build();

                CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
                db = new StdCouchDbConnector("js_homepage", dbInstance);

            } catch (Exception e) {
                Logger.error("Unable to connect to database.", e);
            }
        }

        return db;
    }

    /*
     * (non-Javadoc)
     * 
     * @see service.database.DatabaseService#findUserByName(java.lang.String)
     */
    @Override
    public User findUserByName(String name) {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("user").key(name);
        return getUniqueDocument(User.class, query);
    }

    @Override
    public User upsertUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Project findProjectById(String projectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Project upsertProject(Project project) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see service.database.DatabaseService#findAllProjects()
     */
    @Override
    public List<Project> findAllProjects() {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("projects").key("project");
        return getListOfDocument(Project.class, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see service.database.DatabaseService#findProjectsForStartPage()
     */
    @Override
    public List<Project> findProjectsForStartPage() {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("projects").key("startpage");
        return getListOfDocument(Project.class, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see service.database.DatabaseService#findProjectsForCurrent()
     */
    @Override
    public List<Project> findProjectsForCurrent() {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("projects").key("current");
        return getListOfDocument(Project.class, query);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * service.database.DatabaseService#findProjectsOfType(java.lang.String)
     */
    @Override
    public List<Project> findProjectsOfType(ProjectType type) {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("projects").key(type.toString());
        return getListOfDocument(Project.class, query);
    }

    public <A> A getById(Class<A> clazz, String id) {
        return getDbConnection().get(clazz, id);
    }

    public void createDocument(Object o) {
        getDbConnection().create(o);
    }

    public void updateDocument(Object o) {
        getDbConnection().update(o);
    }

    private static <A> List<A> getListOfDocument(Class<A> clazz, ViewQuery query) {
        final ViewResult result = getDbConnection().queryView(query);

        List<A> list = new ArrayList<A>();
        for (Row row : result.getRows()) {
            list.add(jsonToObject(clazz, row.getValueAsNode()));
        }

        return list;
    }

    private static <A> A getUniqueDocument(Class<A> clazz, ViewQuery query) {
        final ViewResult result = getDbConnection().queryView(query);

        if (result.getRows().size() != 1) {
            return null;
        }

        A a = jsonToObject(clazz, result.getRows().get(0).getValueAsNode());

        return a;
    }

    private static <A> A jsonToObject(Class<A> clazz, JsonNode json) {
        return objectMapper.convertValue(json, clazz);
    }

}
