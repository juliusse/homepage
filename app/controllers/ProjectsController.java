package controllers;

import java.io.IOException;
import java.util.List;

import models.Project;
import models.forms.ProjectFormData;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProjectsController extends Controller {
    
    public static final Form<ProjectFormData> projectForm = Form.form(ProjectFormData.class);
    
    public static Result index(String langKey) {
        Application.setSessionLang(langKey);
        //Project p = new Project("Test", "Test with linebreak <br> break", "English", "Test with linebreak \n break", Arrays.asList(new String[] {"SQL","Play","eBeans"}), new DateTime(), DateTime.now(), null, null, new ArrayList<ProjectType>());
//        List<Project> list = new ArrayList<Project>();
//        list.add(p);
        List<Project> projects = Project.findAll();
        Logger.debug("number of Projects: "+projects.size());
        return ok(views.html.projects.render(Project.findAll()));
    }
    
    public static Result index2(String langKey,String type) {
        return TODO;
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
    
    //POST
    public static Result addProject() throws IOException {
        Form<ProjectFormData> filledForm = projectForm.bindFromRequest();
        Logger.debug(filledForm.data().toString());
        
        if(filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            ProjectFormData data = filledForm.get();
            Project.createFromRequest(data);
            return ok();
        }
        
    }
    
}