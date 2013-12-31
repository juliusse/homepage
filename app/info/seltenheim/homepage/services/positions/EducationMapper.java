package info.seltenheim.homepage.services.positions;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;
import info.seltenheim.homepage.services.database.Education;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("toDate", null);
        return find(attributes);
    }
}
