package info.seltenheim.homepage.services.users;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;

public class UserMapper extends Mapper<User> {

    @Override
    protected DBCollection getCollection() {
        return MongoPlugin.users();
    }

    @Override
    protected User toObject(BasicDBObject bson) {
        final String username = bson.getString("name");
        final String password = bson.getString("password");
        return new User(username, password);
    }

    @Override
    protected BasicDBObject toBson(User user) {
        return doc("name", user.getUsername()).append("password", user.getPassword());
    }

    @Override
    protected BasicDBObject getDefaultSearchFilter() {
        return MongoPlugin.doc();
    }

    @Override
    protected DBCursor sortResults(DBCursor unsortedResults) {
        return unsortedResults;
    }

}
