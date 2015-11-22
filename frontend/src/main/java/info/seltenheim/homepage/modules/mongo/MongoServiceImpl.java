package info.seltenheim.homepage.modules.mongo;

import com.mongodb.*;
import org.apache.commons.lang3.Validate;
import org.bson.types.ObjectId;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MongoServiceImpl implements MongoService {

    private MongoClient mongoClient;
    private DB database;

    @Inject
    public MongoServiceImpl(Configuration configuration) {

        try {
            final String host = host(configuration);
            final Integer port = port(configuration);
            Logger.info(String.format("Starting MongoClient for %s:%d", host, port));
            mongoClient = new MongoClient(host, port);
            mongoClient.setWriteConcern(WriteConcern.FSYNC_SAFE);
            database = mongoClient.getDB(configuration.getString("mongo.db.default.name"));
            ensureIndexes();

        } catch (UnknownHostException e) {
            Logger.error("can't connect ");
        }


    }

    public DBCollection projects() {
        return database.getCollection("projects");
    }

    public DBCollection users() {
        return database.getCollection("users");
    }

    public DBCollection skillGroups() {
        return database.getCollection("skillGroups");
    }

    public DBCollection positions() {
        return database.getCollection("positions");
    }

    public DBCollection trackings() {
        return database.getCollection("trackings");
    }

    public BasicDBObject doc() {
        return new BasicDBObject();
    }

    public BasicDBObject doc(final String key, final Object value) {
        return new BasicDBObject(key, value);
    }

    public List<String> getStringList(final BasicDBObject bson, final String key) {
        final Object maybeList = bson.get(key);
        List<String> result = null;
        if (maybeList != null && maybeList instanceof BasicDBList) {
            final BasicDBList bsonList = (BasicDBList) maybeList;
            result = new ArrayList<String>(bsonList.size());
            for (final Object item : bsonList) {
                result.add(item.toString());
            }
        }
        return result;
    }

    /**
     * Creates a BSON document for the field selecting syntax.
     *
     * @param fields the fields that should be in the document
     * @return
     */
    public BasicDBObject presentFields(String... fields) {
        final BasicDBObject result = new BasicDBObject();
        for (final String field : fields) {
            result.append(field, 1);
        }
        return result;
    }

    public BasicDBObject queryById(final String id) {
        return doc("_id", new ObjectId(id));
    }

    public BasicDBObject queryForFile(String id, String path) {
        return doc("project", new ObjectId(id)).append("path", path);
    }

    private String host(Configuration configuration) {
        final String configKeyMongoHost = "mongo.host";
        final String host = configuration.getString(configKeyMongoHost);
        Validate.notNull(host, configKeyMongoHost + " is mandatory");
        return host;
    }

    private Integer port(Configuration configuration) {
        final String configKeyMongoPort = "mongo.port";
        final Integer port = configuration.getInt(configKeyMongoPort);
        Validate.notNull(port, configKeyMongoPort + " is mandatory");
        return port;
    }

    private void ensureIndexes() {
        projects().ensureIndex(doc("_id", 1).append("projectTypes", 1));
        projects().ensureIndex(doc("projectTypes", 1));
        users().ensureIndex(doc("_id", 1));
    }
}
