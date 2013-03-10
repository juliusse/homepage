package controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Project;
import models.Project.ProjectType;
import models.forms.ProjectFormData;

import org.joda.time.DateTime;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class ProjectsController extends Controller {
    
    public static final Form<ProjectFormData> projectForm = Form.form(ProjectFormData.class);
    
    public static Result index(String langKey) {
        Application.setSessionLang(langKey);
        
        List<Project> projects = Project.findAll();
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
    
    public static Result index2(String langKey,String type) {
        Application.setSessionLang(langKey);
        
        List<Project> projects = Project.findByType(ProjectType.valueOf(type));
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
    
    public static Result image(String projectId) {
        Project proj = Project.findById(projectId);
        if(proj == null) {
            return badRequest();
        }
        
        //response().setContentType("image/jpg");
        return ok(proj.getMainImage());
    }
    
    //GET
    public static Result renderAdd(String langKey) {
        Application.setSessionLang(langKey);

        return ok(views.html.projectAdd.render(projectForm));
    }
    
    //GET
    public static Result renderEdit(String langKey,String projectId) {
        Application.setSessionLang(langKey);
        
        return ok(views.html.projectAdd.render(projectForm.fill(new ProjectFormData(Project.findById(projectId)))));
    }
    
    //POST
    @Security.Authenticated(Secured.class)
    public static Result changeProject() throws IOException {
        Form<ProjectFormData> filledForm = projectForm.bindFromRequest();
        Logger.debug(filledForm.data().toString());
        
        if(filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            ProjectFormData data = filledForm.get();
            Project.createFromRequest(data);
            return redirect(routes.ProjectsController.index(Application.getSessionLang()));
        }
        
    }
   
    
    public static Result deleteProject(String langKey, String projectId) {
        return TODO;
    }
    
}