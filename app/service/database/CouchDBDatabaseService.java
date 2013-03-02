package service.database;

import java.util.ArrayList;
import java.util.List;

import models.Project;
import models.User;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import play.Logger;

public class CouchDBDatabaseService {
    private static CouchDbConnector db;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static CouchDbConnector getDbConnection() {
        if(db == null) {
            try {
                HttpClient httpClient = new StdHttpClient.Builder()
                .url("http://localhost:5984").username("admin").password("admin")
                .build();

                CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
                db = new StdCouchDbConnector("js_homepage", dbInstance);
            } catch (Exception e) {
                Logger.error("Unable to connect to database.",e);
            }
        }

        return db;
    }

    public static User getUserByName(String name) {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("user").key(name);
        return getUniqueDocument(User.class, query);
    }
    
    public static User getUserByMail(String email) {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("user").key(email);
        return getUniqueDocument(User.class, query);
    }
    
    public static User getUserByNameAndPassword(String name, String password) {
        ViewQuery query = new ViewQuery()
                    .designDocId("_design/v1/")
                    .viewName("user")
                    .key(ComplexKey.of(name,password));
        
        User user = getUniqueDocument(User.class, query);
        return user;
    }
    
    public static List<Project> getAllProjects() {
        ViewQuery query = new ViewQuery().designDocId("_design/v1/").viewName("projects").key("project");
        return getListOfDocument(Project.class, query);
    }
    
    public static <A> A getById(Class<A> clazz, String id) {
        return getDbConnection().get(clazz, id);
    }
    
    public static void saveDocument(Object o) {
        getDbConnection().create(o);
    }
    
    private static <A> List<A> getListOfDocument(Class<A> clazz, ViewQuery query) {
        final ViewResult result = getDbConnection().queryView(query);
        
        List<A> list = new ArrayList<A>();
        for(Row row : result.getRows()) {
            list.add(jsonToObject(clazz, row.getValueAsNode()));
        }
        
        return list;
    }
    
    private static <A> A getUniqueDocument(Class<A> clazz, ViewQuery query) {
        final ViewResult result = getDbConnection().queryView(query);
        
        if(result.getRows().size() != 1) {
            return null;
        }
        
        A a = jsonToObject(clazz, result.getRows().get(0).getValueAsNode());
        
        return a;
    }
    
    private static <A> A jsonToObject(Class<A> clazz, JsonNode json) {
        return objectMapper.convertValue(json, clazz);
    }
}
