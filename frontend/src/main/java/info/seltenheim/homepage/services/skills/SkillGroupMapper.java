package info.seltenheim.homepage.services.skills;

import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.modules.mongo.MongoService;
import info.seltenheim.homepage.services.Mapper;

import javax.inject.Inject;

public class SkillGroupMapper extends Mapper<SkillGroup> {
    private final SkillGroupSerializer skillGroupSerializer;

    @Inject
    public SkillGroupMapper(MongoService mongoService, SkillGroupSerializer skillGroupSerializer) {
        super(mongoService, mongoService.skillGroups(), mongoService.doc(), mongoService.doc("displayRank", 1));
        this.skillGroupSerializer = skillGroupSerializer;
    }

    @Override
    protected SkillGroup toObject(BasicDBObject bson) {
        return skillGroupSerializer.deserialize(bson);
    }

    @Override
    protected BasicDBObject toBson(SkillGroup object) {
        return skillGroupSerializer.serialize(object);
    }

}
