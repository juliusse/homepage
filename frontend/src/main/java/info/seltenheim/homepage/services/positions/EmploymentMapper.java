package info.seltenheim.homepage.services.positions;

import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.plugins.mongo.MongoService;
import info.seltenheim.homepage.services.Mapper;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class EmploymentMapper extends Mapper<Employment> {
    private final PositionSerializer positionSerializer;

    @Inject
    public EmploymentMapper(MongoService mongoService, PositionSerializer positionSerializer) {
        super(mongoService, mongoService.positions(), mongoService.doc("type", "employment"), mongoService.doc("fromDate", -1));
        this.positionSerializer = positionSerializer;
    }

    @Override
    protected Employment toObject(BasicDBObject bson) {
        return positionSerializer.deserializeEmployment(bson);
    }

    @Override
    protected BasicDBObject toBson(Employment object) {
        return positionSerializer.serialize(object);
    }

    public List<Employment> findCurrentEmployments() throws IOException {
        return find("toDate", null);
    }
}
