package info.seltenheim.homepage.services.positions;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;

import java.io.IOException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class EmploymentMapper extends Mapper<Employment> {

    @Override
    protected DBCollection getCollection() {
        return MongoPlugin.positions();
    }

    @Override
    protected Employment toObject(BasicDBObject bson) {
        return PositionSerializer.deserializeEmployment(bson);
    }

    @Override
    protected BasicDBObject toBson(Employment object) {
        return PositionSerializer.serialize(object);
    }

    @Override
    protected BasicDBObject getDefaultSearchFilter() {
        return doc("type", "employment");
    }

    @Override
    protected DBCursor sortResults(DBCursor unsortedResults) {
        return unsortedResults.sort(doc("fromDate", -1));
    }

    public List<Employment> findCurrentEmployments() throws IOException {
        return find("toDate", null);
    }
}
