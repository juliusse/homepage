package info.seltenheim.homepage.services.positions;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;

import com.mongodb.BasicDBObject;

public class PositionSerializer {
    private static final DateTimeFormatter persistenceDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final int VERSION_EDUCATION = 1;
    private static final int VERSION_EMPLOYMENT = 2;

    private enum Type {
        employment, education
    };

    private static BasicDBObject serializePosition(Position position) {
        Logger.debug(position.getFromDate().toString());
        final BasicDBObject document = doc("type", position instanceof Employment ? "employment" : "education").append("fromDate", position.getFromDate().toString(persistenceDateTimeFormatter))
                .append("place", position.getPlace()).append("website", position.getWebsite()).append("titleMap", position.getTitleMap());

        if (position.getToDate() != null) {
            document.append("toDate", position.getToDate().toString(persistenceDateTimeFormatter));
        }

        return document;
    }

    public static BasicDBObject serialize(Employment employment) {
        return serializePosition(employment).append("version", VERSION_EMPLOYMENT).append("tasksMap", employment.getTasksMap()).append("technologies", employment.getTechnologies());
    }

    public static BasicDBObject serialize(Education education) {
        return serializePosition(education).append("version", VERSION_EDUCATION).append("degreeMap", education.getDegreeMap()).append("score", education.getScore());
    }

    @SuppressWarnings("unchecked")
    public static Education deserializeEducation(BasicDBObject bson) {
        Validate.notNull(bson);

        final Type type = Type.valueOf(bson.getString("type"));
        Validate.isTrue(type == Type.education);

        final String idFromBson = bson.get("_id").toString();

        final String place = bson.getString("place");
        final DateTime fromDate = persistenceDateTimeFormatter.parseDateTime((bson.getString("fromDate")));

        final String toDateString = bson.getString("toDate");
        final DateTime toDate = toDateString == null ? null : persistenceDateTimeFormatter.parseDateTime(toDateString);
        final String website = bson.getString("website");
        final Map<String, String> titleMap = (Map<String, String>) bson.get("titleMap");

        final Map<String, String> degreeMap = (Map<String, String>) bson.get("degreeMap");
        final String degreeNote = bson.getString("degreeNote");
        final String score = bson.getString("score");

        final Education education = new Education(fromDate, toDate, place, website, score, titleMap, degreeMap, degreeNote);
        education.setId(idFromBson);

        return education;
    }

    @SuppressWarnings("unchecked")
    public static Employment deserializeEmployment(BasicDBObject bson) {
        Validate.notNull(bson);

        final Type type = Type.valueOf(bson.getString("type"));
        Validate.isTrue(type == Type.employment);

        final String idFromBson = bson.get("_id").toString();
        final int version = bson.getInt("version", 1);

        final String place = bson.getString("place");
        final DateTime fromDate = persistenceDateTimeFormatter.parseDateTime((bson.getString("fromDate")));

        final String toDateString = bson.getString("toDate");
        final DateTime toDate = toDateString == null ? null : persistenceDateTimeFormatter.parseDateTime(toDateString);
        final String website = bson.getString("website");
        final Map<String, String> titleMap = (Map<String, String>) bson.get("titleMap");

        final Map<String, List<String>> tasksMap = (Map<String, List<String>>) bson.get("tasksMap");

        // introduced in version 2
        List<String> technologies = new ArrayList<String>();
        if (version >= 2) {
            technologies = (List<String>) bson.get("technologies");
        }

        final Employment employment = new Employment(fromDate, toDate, place, titleMap, website, tasksMap, technologies);
        employment.setId(idFromBson);

        return employment;
    }

}
