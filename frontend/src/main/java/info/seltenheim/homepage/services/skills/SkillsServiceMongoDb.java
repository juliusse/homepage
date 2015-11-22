package info.seltenheim.homepage.services.skills;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

public class SkillsServiceMongoDb implements SkillGroupService {
    private final SkillGroupMapper skillGroupMapper;

    @Inject
    public SkillsServiceMongoDb(SkillGroupMapper skillGroupMapper) {
        this.skillGroupMapper = skillGroupMapper;
    }

    @Override
    public SkillGroup findSkillGroupById(String skillGroupId) throws IOException {
        return skillGroupMapper.findById(skillGroupId);
    }

    @Override
    public List<SkillGroup> findAllSkillGroups() throws IOException {
        return skillGroupMapper.findAll();
    }

    @Override
    public SkillGroup upsertSkillGroup(SkillGroup skillGroup) throws IOException {
        return skillGroupMapper.upsert(skillGroup);
    }

    @Override
    public SkillGroup addSkillToGroup(String skillGroupId, Skill skill) throws IOException {
        final SkillGroup skillGroup = findSkillGroupById(skillGroupId);
        skillGroup.addSkill(skill);
        return upsertSkillGroup(skillGroup);
    }
}
