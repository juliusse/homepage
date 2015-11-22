package info.seltenheim.homepage.plugins.mongo;

import com.google.inject.ImplementedBy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.util.List;

@ImplementedBy(MongoServiceImpl.class)
public interface MongoService {

    public DBCollection projects();

    public DBCollection users();

    public DBCollection skillGroups();

    public DBCollection positions();

    public DBCollection trackings();

    public BasicDBObject doc();

    public BasicDBObject doc(final String key, final Object value);

    public List<String> getStringList(final BasicDBObject bson, final String key);

    /**
     * Creates a BSON document for the field selecting syntax.
     *
     * @param fields the fields that should be in the document
     * @return
     */
    public BasicDBObject presentFields(String... fields);

    public BasicDBObject queryById(final String id);

    public BasicDBObject queryForFile(String id, String path);
}
