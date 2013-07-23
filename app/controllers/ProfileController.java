package controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.forms.UpsertEducationData;
import models.forms.UpsertEmploymentData;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.database.DatabaseService;
import services.database.Education;
import services.database.Employment;
import controllers.secured.OnlyLoggedIn;

@Component
public class ProfileController extends Controller {

    @Autowired
    private DatabaseService databaseService;
    
    
    public Result index(String langKey, String positionId) throws IOException {
        Application.setSessionLang(langKey);

        final List<Employment> empl = databaseService.findAllEmployments();
        final List<Education> edus = databaseService.findAllEducations();
        
        Collections.sort(empl, new Comparator<Employment>() {
            @Override
            public int compare(Employment o1, Employment o2) {
                final DateTime end1 = o1.getToDate();
                final DateTime end2 = o2.getToDate();
                if(end1 == null && end2 == null) {
                    return 0;
                } else if(end1 == null) {
                    return -1;
                } else if(end2 == null) {
                    return 1;
                } else {
                    return -end1.compareTo(end2);
                }
            }
        });

        return ok(views.html.profile.render(empl, edus, positionId));
    }
    
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result upsertEmployment(String langKey, String employmentId) throws IOException {
        Form<UpsertEmploymentData> filledForm = Form.form(UpsertEmploymentData.class).bindFromRequest();
        
        if(filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            UpsertEmploymentData data = filledForm.get();
            final Employment employment = employmentId != "-1" ? databaseService.findEmploymentById(employmentId) : new Employment();

            employment.setFromDate(data.getFromDateObject());
            employment.setToDate(data.getToDateObject());
            employment.setPlace(data.getPlace());
            employment.setTitle("de", data.getTitleDe());
            employment.setTitle("en", data.getTitleEn());
            employment.setWebsite(data.getWebsite());
            employment.setTasks("de", data.getTasksDeList());
            employment.setTasks("en", data.getTasksEnList());
            
            databaseService.upsertPosition(employment);
            return redirect(routes.ProfileController.index(langKey,""));
        }
    }
    
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result upsertEducation(String langKey, String educationId) throws IOException {
        Form<UpsertEducationData> filledForm = Form.form(UpsertEducationData.class).bindFromRequest();
        
        if(filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            UpsertEducationData data = filledForm.get();
            final Education education = educationId != "-1" ? databaseService.findEducationById(educationId) : new Education();

            education.setFromDate(data.getFromDateObject());
            education.setToDate(data.getToDateObject());
            education.setPlace(data.getPlace());
            education.setTitle("de", data.getTitleDe());
            education.setTitle("en", data.getTitleEn());
            education.setWebsite(data.getWebsite());
            education.setDegree("de", data.getDegreeDe());
            education.setDegree("en", data.getDegreeEn());
            education.setScore(data.getScore());
            
            databaseService.upsertPosition(education);
            return redirect(routes.ProfileController.index(langKey,""));
        }
    }

}
