package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.services.positions.Education;
import info.seltenheim.homepage.services.positions.Employment;
import info.seltenheim.homepage.services.positions.PositionsService;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

public class ProfileController extends Controller {

    @Inject
    private PositionsService positionsService;


    public Result index(String langKey, String positionId) throws IOException {

        final List<Employment> empl = positionsService.findAllEmployments();
        final List<Education> edus = positionsService.findAllEducations();

        return ok(info.seltenheim.homepage.views.html.profile.render(empl, edus, positionId));
    }

    public Result getEmployments() throws IOException {
        return ok(Json.toJson(positionsService.findAllEmployments()));
    }

    public Result getEducations() throws IOException {
        return ok(Json.toJson(positionsService.findAllEducations()));
    }

    public Result upsertEmployment() throws IOException {
        final Employment employment = Json.fromJson(request().body().asJson(), Employment.class);
        positionsService.upsertPosition(employment);
        return ok();
    }

    public Result upsertEducation() throws IOException {
        final Education education = Json.fromJson(request().body().asJson(), Education.class);
        positionsService.upsertPosition(education);
        return ok();
    }
/*
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result upsertEmployment2(String langKey) throws IOException {
        Form<EmploymentData> filledForm = Form.form(EmploymentData.class).bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final EmploymentData data = filledForm.get();
            final String employmentId = data.getId();
            final Employment employment = !employmentId.equals("-1") ? positionsService.findEmploymentById(employmentId) : new Employment();

            employment.setFromDate(data.getFromDateObject());
            employment.setToDate(data.getToDateObject());
            employment.setPlace(data.getPlace());
            employment.setTitle("de", data.getTitleDe());
            employment.setTitle("en", data.getTitleEn());
            employment.setWebsite(data.getWebsite());
            employment.setTasks("de", data.getTasksDeList());
            employment.setTasks("en", data.getTasksEnList());
            employment.setTechnologies(data.getTechnologiesList());

            positionsService.upsertPosition(employment);
            return redirect(routes.ProfileController.index(langKey, ""));
        }
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result upsertEducation2(String langKey) throws IOException {
        Form<EducationData> filledForm = Form.form(EducationData.class).bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final EducationData data = filledForm.get();
            final String educationId = data.getId();

            final Education education = educationId != "-1" ? positionsService.findEducationById(educationId) : new Education();

            education.setFromDate(data.getFromDateObject());
            education.setToDate(data.getToDateObject());
            education.setPlace(data.getPlace());
            education.setTitle("de", data.getTitleDe());
            education.setTitle("en", data.getTitleEn());
            education.setWebsite(data.getWebsite());
            education.setDegree("de", data.getDegreeDe());
            education.setDegree("en", data.getDegreeEn());
            education.setScore(data.getScore());

            positionsService.upsertPosition(education);
            return redirect(routes.ProfileController.index(langKey, ""));
        }


    }
    */
}
