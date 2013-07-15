package controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Project;
import models.forms.ProjectFormData;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import service.database.DatabaseService;
import controllers.secured.OnlyLoggedIn;

@Component
public class ProjectsController extends Controller {
    
    @Autowired
    private DatabaseService databaseService;
    
    public static final Form<ProjectFormData> projectForm = Form.form(ProjectFormData.class);
    
    public Result index(String langKey) {
        Application.setSessionLang(langKey);
        
        List<Project> projects = databaseService.findAllProjects();
        Collections.sort(projects, new Comparator<Project>() {

            @Override
            public int compare(Project o1, Project o2) {
                final DateTime p1End = o1.getDevelopmentEnd();
                final DateTime p2End = o2.getDevelopmentEnd();
                if(p1End == null && p2End == null) {
                    return 0;
                } else if(p1End == null) {
                    return -1;
                } else if(p2End == null) {
                    return 1;
                } else {
                    return -p1End.compareTo(p2End);
                }
            }
        });
        Logger.debug("number of Projects: "+projects.size());
        return ok(views.html.projects.render(projects));
    }
    
    public Result index2(String langKey,String type) {
        Application.setSessionLang(langKey);
        
        List<Project> projects = databaseService.findProjectsOfType(type);
        Collections.sort(projects, new Comparator<Project>() {

            @Override
            public int compare(Project o1, Project o2) {
                final DateTime p1End = o1.getDevelopmentEnd();
                final DateTime p2End = o2.getDevelopmentEnd();
                if(p1End == null && p2End == null) {
                    return 0;
                } else if(p1End == null) {
                    return -1;
                } else if(p2End == null) {
                    return 1;
                } else {
                    return -p1End.compareTo(p2End);
                }
            }
        });
        Logger.debug("number of Projects: "+projects.size());
        return ok(views.html.projects.render(projects));
    }
    
    //GET
    public Result renderAdd(String langKey) {
        Application.setSessionLang(langKey);

        return ok(views.html.projectAdd.render(projectForm));
    }
    
    //GET
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result renderEdit(String langKey,String projectId) {
        Application.setSessionLang(langKey);
        
        return ok(views.html.projectAdd.render(projectForm.fill(new ProjectFormData(databaseService.findProjectById(projectId)))));
    }
    
    //POST
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result changeProject() throws IOException {
        Form<ProjectFormData> filledForm = projectForm.bindFromRequest();
        Logger.debug(filledForm.data().toString());
        
        if(filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            ProjectFormData data = filledForm.get();
            //TODO merge project from request and from db
            //Project.createFromRequest(data);
            return redirect(routes.ProjectsController.index(Application.getSessionLang()));
        }
        
    }
    
}