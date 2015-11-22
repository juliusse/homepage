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

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.twirl.api.Html;

import javax.inject.Inject;

public class SkillsController extends Controller {
    private final static Form<AddSkillGroupData> addSkillGroupForm = Form.form(AddSkillGroupData.class);
    private final static Form<AddSkillData> addSkillForm = Form.form(AddSkillData.class);
    private final static Form<SetSkillLevelData> setSkillLevelForm = Form.form(SetSkillLevelData.class);

    private final SkillGroupService skillGroupService;

    @Inject
    public SkillsController(SkillGroupService skillGroupService) {
        this.skillGroupService = skillGroupService;
    }

    public Result index(String langKey) throws IOException {
        final List<SkillGroup> skillGroups = skillGroupService.findAllSkillGroups();
        final boolean isDe = langKey.equals("de");

        final String description = isDe ? "<p>" + "Hier finden sie einen Auszug aus der Liste meiner technischen Qualifikationen.<br>" + "Auf Soft Skills gehe ich an dieser Stelle nicht ein.<br>"
                + "Jedoch stehe ich gerne für ein Gespräch bereit, in dem sie sich ein Bild von meiner Persönlichkeit machen können." + "</p>" : "<p>"
                + "This page contains an excerpt of my technical qualifications.<br>" + "Feel free to contact me to get an image of my personality." + "</p>";

        return ok(info.seltenheim.homepage.views.html.skills.render(Html.apply(description), skillGroups));
    }

    public Result getSkillGroups() throws IOException {
        return ok(Json.toJson(skillGroupService.findAllSkillGroups()));
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result updateSkillGroup(String id) throws IOException {
        final SkillGroup skillGroup = Json.fromJson(request().body().asJson(), SkillGroup.class);
        return ok(Json.toJson(skillGroupService.upsertSkillGroup(skillGroup)));
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

            return redirect(routes.SkillsController.index(lang().language()));
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

            return redirect(routes.SkillsController.index(lang().language()));
        }
    }
}
