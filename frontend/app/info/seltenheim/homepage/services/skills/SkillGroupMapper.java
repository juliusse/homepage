package info.seltenheim.homepage.services.skills;

import info.seltenheim.homepage.plugins.mongo.MongoPlugin;
import info.seltenheim.homepage.services.Mapper;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class SkillGroupMapper extends Mapper<SkillGroup> {

    @Override
    protected DBCollection getCollection() {
        return MongoPlugin.skillGroups();
    }

    @Override
    protected SkillGroup toObject(BasicDBObject bson) {
        return SkillGroupSerializer.deserialize(bson);
    }

    @Override
    protected BasicDBObject toBson(SkillGroup object) {
        return SkillGroupSerializer.serialize(object);
    }

    @Override
    protected BasicDBObject getDefaultSearchFilter() {
        return MongoPlugin.doc();
    }

    @Override
    protected DBCursor sortResults(DBCursor unsortedResults) {
        return unsortedResults.sort(MongoPlugin.doc("displayRank", 1));
    }

}
