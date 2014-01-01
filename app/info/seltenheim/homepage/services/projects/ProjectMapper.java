package info.seltenheim.homepage.services.projects;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;

public class ProjectMapper extends Mapper<Project> {

    @Override
    protected DBCollection getCollection() {
        return MongoPlugin.projects();
    }

    @Override
    protected Project toObject(BasicDBObject bson) {
        return ProjectsSerializer.deserialize(bson);
    }

    @Override
    protected BasicDBObject toBson(Project object) {
        return ProjectsSerializer.serialize(object);
    }

    @Override
    protected BasicDBObject getDefaultSearchFilter() {
        return MongoPlugin.doc();
    }

    @Override
    protected DBCursor sortResults(DBCursor unsortedResults) {
        return unsortedResults.sort(doc("developmentEnd", -1));
    }

}
