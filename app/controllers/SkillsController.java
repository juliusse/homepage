package controllers;

import java.io.IOException;
import java.util.List;

import models.forms.AddSkillData;
import models.forms.AddSkillGroupData;
import models.forms.SetSkillLevelData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import controllers.secured.OnlyLoggedIn;

import play.api.templates.Html;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.database.DatabaseService;
import services.database.Skill;
import services.database.SkillGroup;

@Component
public class SkillsController extends Controller {
    private final static Form<AddSkillGroupData> addSkillGroupForm = Form.form(AddSkillGroupData.class);
    private final static Form<AddSkillData> addSkillForm = Form.form(AddSkillData.class);
    private final static Form<SetSkillLevelData> setSkillLevelForm = Form.form(SetSkillLevelData.class);

    @Autowired
    private DatabaseService databaseService;

    public Result index(String langKey) throws IOException {
        Application.setSessionLang(langKey);

        final List<SkillGroup> skillGroups = databaseService.findAllSkillGroups();
        final boolean isDe = langKey.equals("de");

        final String description = isDe ? "<p>" + "Hier finden sie einen Auszug aus der Liste meiner technischen Qualifikationen.<br>" + "Auf Soft Skills gehe ich an dieser Stelle nicht ein.<br>"
                + "Jedoch stehe ich gerne für ein Gespräch bereit, in dem sie sich ein Bild von meiner Persönlichkeit machen können." + "</p>" : "<p>"
                + "This page contains an excerpt of my technical qualifications.<br>" + "Feel free to contact me to get an image of my personality." + "</p>";

        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("C#", 0.75));
        // skills.add(new Skill("PHP", 0.75));
        // skills.add(new Skill("Java", 0.8));
        // skills.add(new Skill("C/C++", 0.15));
        // skills.add(new Skill("Javascript", 0.65));
        // skills.add(new Skill("CoffeScript", 0.30));
        // skillGroups.add(new SkillGroup(isDe ? "Programmiersprachen" :
        // "Programming Languages", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("Visual Studio", 0.65));
        // skills.add(new Skill("Eclipse", 0.7));
        // skills.add(new Skill("NetBeans", 0.6));
        // skillGroups.add(new SkillGroup("IDEs", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("Windows", 1));
        // skills.add(new Skill("Mac OS X 10.x", 0.3));
        // skills.add(new Skill("Linux", 0.4));
        // skillGroups.add(new SkillGroup(isDe ? "Betriebssysteme" :
        // "Operation Systems", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill(isDe ? "Relationelle Datenbanken entwerfen" :
        // "Database Engineering", 0.75));
        // skills.add(new Skill("MySQL", 0.7));
        // skills.add(new Skill("MSSQL", 0.4));
        // skillGroups.add(new SkillGroup(isDe ? "Netzwerke & Datenbanken" :
        // "Networking & Databases", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("Play! Framework 2", 0.75));
        // skills.add(new Skill("Akka", 0.75));
        // skills.add(new Skill("Zend Framework 1", 0.3));
        // skills.add(new Skill("Zend Framework 2", 0.4));
        // skills.add(new Skill("Twitter Bootstrap", 0.6));
        // skills.add(new Skill("jQuery", 0.6));
        // skills.add(new Skill("XNA 4", 0.55));
        // skills.add(new Skill("Processing", 0.90));
        // skillGroups.add(new SkillGroup(isDe ? "Frameworks & Bibliotheken" :
        // "Frameworks & Libraries", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("SVN", 0.85));
        // skills.add(new Skill("Git", 0.70));
        // skillGroups.add(new SkillGroup(isDe ? "Versionierungstools" :
        // "Source Code Management", skills));
        //
        // skills = new ArrayList<Skill>();
        // skills.add(new Skill("Maven", -1));
        // skills.add(new Skill("Ivy", -1));
        // skills.add(new Skill("Sbt", -1));
        // skillGroups.add(new SkillGroup("Dependency Management & Build tools",
        // skills));

        return ok(views.html.skills.render(Html.apply(description), skillGroups));
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

            databaseService.upsertSkillGroup(skillGroup);

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

            databaseService.addSkillToGroup(skillGroupId, skill);

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
            
            final SkillGroup skillGroup = databaseService.findSkillGroupById(skillGroupId);
            for(Skill skill : skillGroup.getSkills()) {
                if(skill.getName().equals(skillName)) {
                    skill.setKnowledge(data.getLevel());
                    break;
                }
            }

            databaseService.upsertSkillGroup(skillGroup);

            return redirect(routes.SkillsController.index(Application.getSessionLang()));
        }
    }
}
