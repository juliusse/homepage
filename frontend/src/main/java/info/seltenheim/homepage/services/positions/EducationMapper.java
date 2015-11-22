package info.seltenheim.homepage.services.positions;


import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.modules.mongo.MongoService;
import info.seltenheim.homepage.services.Mapper;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class EducationMapper extends Mapper<Education> {
    private final PositionSerializer positionSerializer;

    @Inject
    public EducationMapper(MongoService mongoService, PositionSerializer positionSerializer) {
        super(mongoService, mongoService.positions(), mongoService.doc("type", "education"), mongoService.doc("fromDate", -1));
        this.positionSerializer = positionSerializer;
    }


    @Override
    protected Education toObject(BasicDBObject bson) {
        return positionSerializer.deserializeEducation(bson);
    }

    @Override
    protected BasicDBObject toBson(Education object) {
        return positionSerializer.serialize(object);
    }

    public List<Education> findCurrentEducations() throws IOException {
        return find("toDate", null);
    }
}
