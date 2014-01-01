package info.seltenheim.homepage.services.positions;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;

import java.io.IOException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class EducationMapper extends Mapper<Education> {

    @Override
    protected DBCollection getCollection() {
        return MongoPlugin.positions();
    }

    @Override
    protected Education toObject(BasicDBObject bson) {
        return PositionSerializer.deserializeEducation(bson);
    }

    @Override
    protected BasicDBObject toBson(Education object) {
        return PositionSerializer.serialize(object);
    }

    @Override
    protected BasicDBObject getDefaultSearchFilter() {
        return doc("type", "education");
    }

    @Override
    protected DBCursor sortResults(DBCursor unsortedResults) {
        return unsortedResults.sort(doc("fromDate", -1));
    }

    public List<Education> findCurrentEducations() throws IOException {
        return find("toDate",null);
    }
}
