package info.seltenheim.homepage.services.skills;

import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.doc;
import static info.seltenheim.homepage.plugins.mongo.MongoPlugin.getStringList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;

import com.mongodb.BasicDBObject;

public class SkillGroupSerializer {
    public static BasicDBObject serialize(SkillGroup skillGroup) {

        return doc("displayRank", skillGroup.getDisplayRank()).append("skills", convertSkillsListToSkillsJsonStrings(skillGroup.getSkills())).append("nameMap", skillGroup.getNameMap());
    }

    public static SkillGroup deserialize(BasicDBObject bson) {
        SkillGroup result = null;
        if (bson != null) {
            final String idFromBson = bson.get("_id").toString();
            final Integer displayRank = bson.getInt("displayRank");

            final List<String> skillStrings = getStringList(bson, "skills");

            @SuppressWarnings("unchecked")
            final Map<String, String> nameMap = (Map<String, String>) bson.get("nameMap");
            result = new SkillGroup(nameMap, convertSkillsJsonStringsToSkills(skillStrings));
            result.setId(idFromBson);
            result.setDisplayRank(displayRank);
        }
        return result;
    }

    private static List<Skill> convertSkillsJsonStringsToSkills(List<String> skillsJson) {
        final List<Skill> skills = new ArrayList<Skill>();
        final ObjectMapper mapper = new ObjectMapper();
        for (String skillString : skillsJson) {
            try {
                skills.add(mapper.readValue(skillString, Skill.class));
            } catch (Exception e) {
                Logger.error("error parsing skill!", e);
            }
        }
        return skills;
    }

    private static List<String> convertSkillsListToSkillsJsonStrings(List<Skill> skills) {
        final List<String> skillsJson = new ArrayList<String>();
        final ObjectMapper mapper = new ObjectMapper();
        for (Skill skill : skills) {
            try {
                skillsJson.add(mapper.writeValueAsString(skill));
            } catch (Exception e) {
                Logger.error("error parsing skill!", e);
            }
        }
        return skillsJson;
    }
}
