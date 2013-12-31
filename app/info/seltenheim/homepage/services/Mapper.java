package info.seltenheim.homepage.services;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.queryById;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

public abstract class Mapper<T> {

    /**
     * The collection where the results should be searched in
     * 
     * @return
     */
    protected abstract DBCollection getCollection();

    protected abstract T toObject(BasicDBObject bson);

    protected abstract BasicDBObject toBson(T object);

    /**
     * 
     * @return a document with all default filters like a "type" or a "version"
     */
    protected abstract BasicDBObject getDefaultSearchFilter();

    protected abstract DBCursor sortResults(DBCursor unsortedResults);

    public List<T> findAll() throws IOException {
        return find(new HashMap<String, String>());
    }

    public T findById(String id) throws IOException {
        try {
            final BasicDBObject query = queryById(id);
            final BasicDBObject objectBson = (BasicDBObject) getCollection().findOne(query);
            Logger.debug(objectBson.toString());
            return toObject(objectBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public List<T> find(Map<String, String> attributes) throws IOException {
        try {
            final List<T> objects = new ArrayList<T>();
            final BasicDBObject query = getDefaultSearchFilter();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                query.append(entry.getKey(), entry.getValue());
            }

            DBCursor cursor = null;
            try {
                cursor = getCollection().find(query);
                cursor = sortResults(cursor);

                for (DBObject dbObject : cursor) {
                    if (dbObject instanceof BasicDBObject) {
                        objects.add(toObject((BasicDBObject) dbObject));
                    }
                }
            } finally {
                cursor.close();
            }

            return objects;
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public T upsert(T object) throws IOException {
        try {
            final DBCollection collection = getCollection();
            final BasicDBObject document = toBson(object);
            String id = document.getString("_id", "");

            if (id.isEmpty()) { // insert
                collection.insert(document);
                id = document.get("_id").toString();

            } else {
                collection.update(queryById(id), document);
            }

            return findById(id);

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }
}
