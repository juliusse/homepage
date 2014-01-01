package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.controllers.secured.OnlyLoggedIn;
import info.seltenheim.homepage.models.forms.AddSkillData;
import info.seltenheim.homepage.models.forms.AddSkillGroupData;
import info.seltenheim.homepage.models.forms.SetSkillLevelData;
import info.seltenheim.homepage.services.skills.Skill;
import info.seltenheim.homepage.services.skills.SkillGroup;
import info.seltenheim.homepage.services.skills.SkillGroupService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.api.templates.Html;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Component
public class SkillsController extends Controller {
    private final static Form<AddSkillGroupData> addSkillGroupForm = Form.form(AddSkillGroupData.class);
    private final static Form<AddSkillData> addSkillForm = Form.form(AddSkillData.class);
    private final static Form<SetSkillLevelData> setSkillLevelForm = Form.form(SetSkillLevelData.class);

    @Autowired
    private SkillGroupService skillGroupService;

    public Result index(String langKey) throws IOException {
        final List<SkillGroup> skillGroups = skillGroupService.findAllSkillGroups();
        final boolean isDe = langKey.equals("de");

        final String description = isDe ? "<p>" + "Hier finden sie einen Auszug aus der Liste meiner technischen Qualifikationen.<br>" + "Auf Soft Skills gehe ich an dieser Stelle nicht ein.<br>"
                + "Jedoch stehe ich gerne für ein Gespräch bereit, in dem sie sich ein Bild von meiner Persönlichkeit machen können." + "</p>" : "<p>"
                + "This page contains an excerpt of my technical qualifications.<br>" + "Feel free to contact me to get an image of my personality." + "</p>";

        return ok(info.seltenheim.homepage.views.html.skills.render(Html.apply(description), skillGroups));
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result addSkillGroup(String langKey) throws IOException {
        Form<AddSkillGroupData> filledForm = addSkillGroupForm.bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final AddSkillGroupData data = filledForm.get();
            final SkillGroup skillGroup = new SkillGroup();

            skillGroup.setName("de", data.getNameDe());
            skillGroup.setName("en", data.getNameEn());

            skillGroupService.upsertSkillGroup(skillGroup);

            return redirect(routes.SkillsController.index(Application.getSessionLang()));
        }
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result addSkill(String langKey, String skillGroupId) throws IOException {
        Form<AddSkillData> filledForm = addSkillForm.bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final AddSkillData data = filledForm.get();
            final Skill skill = new Skill(data.getName(), 0.5);

            skillGroupService.addSkillToGroup(skillGroupId, skill);

            return redirect(routes.SkillsController.index(Application.getSessionLang()));
        }
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result setSkillLevel(String langKey, String skillGroupId, String skillName) throws IOException {
        Form<SetSkillLevelData> filledForm = setSkillLevelForm.bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final SetSkillLevelData data = filledForm.get();

            final SkillGroup skillGroup = skillGroupService.findSkillGroupById(skillGroupId);
            for (Skill skill : skillGroup.getSkills()) {
                if (skill.getName().equals(skillName)) {
                    skill.setKnowledge(data.getLevel());
                    break;
                }
            }

            skillGroupService.upsertSkillGroup(skillGroup);

            return redirect(routes.SkillsController.index(Application.getSessionLang()));
        }
    }
}
