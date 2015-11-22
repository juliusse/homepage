package info.seltenheim.homepage.services.skills;


import java.io.IOException;
import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(SkillsServiceMongoDb.class)
public interface SkillGroupService {
    // SkillGroup methods
    SkillGroup findSkillGroupById(String skillGroupId) throws IOException;

    List<SkillGroup> findAllSkillGroups() throws IOException;

    SkillGroup upsertSkillGroup(SkillGroup skillGroup) throws IOException;

    SkillGroup addSkillToGroup(String skillGroupId, Skill skill) throws IOException;

}
