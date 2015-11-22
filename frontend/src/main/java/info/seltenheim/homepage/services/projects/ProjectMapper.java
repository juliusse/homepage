package info.seltenheim.homepage.services.projects;

import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.modules.mongo.MongoService;
import info.seltenheim.homepage.services.Mapper;

import javax.inject.Inject;

public class ProjectMapper extends Mapper<Project> {
    private final ProjectsSerializer projectsSerializer;

    @Inject
    public ProjectMapper(MongoService mongoService, ProjectsSerializer projectsSerializer) {
        super(mongoService, mongoService.projects(), mongoService.doc(), mongoService.doc("developmentEnd", -1));
        this.projectsSerializer = projectsSerializer;
    }

    @Override
    protected Project toObject(BasicDBObject bson) {
        return projectsSerializer.deserialize(bson);
    }

    @Override
    protected BasicDBObject toBson(Project object) {
        return projectsSerializer.serialize(object);
    }

}
