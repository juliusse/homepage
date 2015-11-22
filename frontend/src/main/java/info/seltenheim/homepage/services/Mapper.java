package info.seltenheim.homepage.services;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import info.seltenheim.homepage.modules.mongo.MongoService;

public abstract class Mapper<T extends PersistentModel> {

    private final MongoService mongoService;
    private final DBCollection collection;
    private final BasicDBObject searchFilter;
    private final DBObject sortFilter;

    protected Mapper(MongoService mongoService, DBCollection collection, BasicDBObject searchFilter, DBObject sortFilter) {
        this.mongoService = mongoService;
        this.collection = collection;
        this.searchFilter = searchFilter;
        this.sortFilter = sortFilter;
    }


    protected abstract T toObject(BasicDBObject bson);

    protected abstract BasicDBObject toBson(T object);

    public List<T> findAll() throws IOException {
        return find(new HashMap<String, Object>());
    }

    public T findById(String id) throws IOException {
        try {
            final BasicDBObject query = mongoService.queryById(id);
            final BasicDBObject objectBson = (BasicDBObject) collection.findOne(query);
            return toObject(objectBson);
        } catch (MongoException e) {
            throw new IOException(e);
        }
    }

    public List<T> find(Map<String, Object> attributes) throws IOException {
        try {
            final List<T> objects = new ArrayList<T>();
            final BasicDBObject query = searchFilter;
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                query.append(entry.getKey(), entry.getValue());
            }

            DBCursor cursor = null;
            try {
                cursor = collection.find(query);
                cursor = cursor.sort(sortFilter);

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

    public List<T> find(String attribute, Object value) throws IOException {
        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(attribute, value);
        return find(attributes);
    }

    public T upsert(T object) throws IOException {
        try {
            final BasicDBObject document = toBson(object);

            String id = object.getId();

            if (id == null || id.isEmpty()) { // insert
                collection.insert(document);
                id = document.get("_id").toString();

            } else {
                collection.update(mongoService.queryById(id), document);
            }

            return findById(id);

        } catch (MongoException e) {
            throw new IOException(e);
        }
    }
}
