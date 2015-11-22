package info.seltenheim.homepage.services.users;

import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.plugins.mongo.MongoService;
import info.seltenheim.homepage.services.Mapper;

import javax.inject.Inject;

public class UserMapper extends Mapper<User> {
    private final MongoService mongoService;


    @Inject
    public UserMapper(MongoService mongoService) {
        super(mongoService, mongoService.users(), mongoService.doc(), mongoService.doc());
        this.mongoService = mongoService;
    }

    @Override
    protected User toObject(BasicDBObject bson) {
        final String username = bson.getString("name");
        final String password = bson.getString("password");
        return new User(username, password);
    }

    @Override
    protected BasicDBObject toBson(User user) {
        return mongoService.doc("name", user.getUsername()).append("password", user.getPassword());
    }
}
