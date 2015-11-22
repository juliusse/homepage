package info.seltenheim.homepage.services.projects;

import com.mongodb.BasicDBObject;
import info.seltenheim.homepage.modules.mongo.MongoService;
import info.seltenheim.homepage.services.projects.Project.ProjectType;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectsSerializer {
    private static final DateTimeFormatter persistenceDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final int VERSION_PROJECT = 1;

    private final MongoService mongoService;

    @Inject
    public ProjectsSerializer(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    @SuppressWarnings("unchecked")
    public Project deserialize(BasicDBObject bson) {
        Validate.notNull(bson);
        Project result = null;

        final String idFromBson = bson.get("_id").toString();
        final boolean displayOnStartPage = Boolean.parseBoolean(bson.get("displayOnStart").toString());

        DateTime developmentStart = null;
        try {
            developmentStart = persistenceDateTimeFormatter.parseDateTime((bson.get("developmentStart").toString()));
        } catch (IllegalArgumentException e) {
            developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime((bson.get("developmentStart").toString()));
        }

        final Object devEndObj = bson.get("developmentEnd");
        DateTime developmentEnd = null;
        try {
            developmentEnd = devEndObj == null ? null : persistenceDateTimeFormatter.parseDateTime(devEndObj.toString());
        } catch (IllegalArgumentException e) {
            developmentEnd = Project.PROJECT_DATETIME_FORMAT.parseDateTime(devEndObj.toString());
        }

        final Object mainImagePathObj = bson.get("mainImagePath");
        final String mainImagePath = mainImagePathObj != null ? mainImagePathObj.toString() : "";

        final String filePath = bson.get("filePath") == null ? null : bson.get("filePath").toString();
        final List<ProjectType> typeOf = convertToProjectTypeList(mongoService.getStringList(bson, "projectTypes"));
        final List<String> technologyList = mongoService.getStringList(bson, "technologies");
        final Map<String, String> titleMap = (Map<String, String>) bson.get("titleMap");
        final Map<String, String> descriptionMap = (Map<String, String>) bson.get("descriptionMap");
        result = new Project(titleMap, descriptionMap, displayOnStartPage, technologyList, developmentStart, developmentEnd, mainImagePath, filePath, typeOf);
        result.setId(idFromBson);

        return result;
    }

    public BasicDBObject serialize(Project project) {
        final BasicDBObject document = mongoService.doc("displayOnStart", project.getDisplayOnStartPage()).append("version", VERSION_PROJECT)
                .append("developmentStart", project.getDevelopmentStart().toString(persistenceDateTimeFormatter)).append("projectTypes", convertToStringList(project.getTypeOf()))
                .append("filePath", project.getFilePath()).append("technologies", project.getTechnologies()).append("titleMap", project.getTitleMap())
                .append("descriptionMap", project.getDescriptionMap()).append("mainImagePath", project.getMainImagePath());

        Logger.debug("mainImagePath: " + project.getMainImagePath());
        if (project.getDevelopmentEnd() != null)
            document.append("developmentEnd", project.getDevelopmentEnd().toString(persistenceDateTimeFormatter));

        return document;
    }

    private List<ProjectType> convertToProjectTypeList(List<String> typeStrings) {
        return typeStrings.stream().map(ProjectType::valueOf).collect(Collectors.toList());
    }

    private <T> List<String> convertToStringList(List<T> list) {
        return list.stream().map(T::toString).collect(Collectors.toList());
    }
}
