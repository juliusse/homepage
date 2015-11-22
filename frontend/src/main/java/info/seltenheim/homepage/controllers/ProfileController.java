package info.seltenheim.homepage.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import info.seltenheim.homepage.comparators.DateRangeComparators;
import info.seltenheim.homepage.controllers.secured.OnlyLoggedIn;
import info.seltenheim.homepage.services.positions.Education;
import info.seltenheim.homepage.services.positions.Employment;
import info.seltenheim.homepage.services.positions.PositionsService;
import info.seltenheim.homepage.services.positions.formdata.EducationData;
import info.seltenheim.homepage.services.positions.formdata.EmploymentData;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class ProfileController extends Controller {

    @Inject
    private PositionsService positionsService;

    public Result index(String langKey, String positionId) throws IOException {

        final List<Employment> empl = positionsService.findAllEmployments();
        final List<Education> edus = positionsService.findAllEducations();

        Collections.sort(empl, DateRangeComparators::compareEndDateTimes);

        return ok(info.seltenheim.homepage.views.html.profile.render(empl, edus, positionId));
    }

    @Security.Authenticated(OnlyLoggedIn.class)
    public Result upsertEmployment(String langKey) throws IOException {
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
    public Result upsertEducation(String langKey) throws IOException {
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

}
