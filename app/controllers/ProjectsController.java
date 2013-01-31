package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Project;
import models.ProjectType;

import org.joda.time.DateTime;

import play.mvc.Controller;
import play.mvc.Result;

public class ProjectsController extends Controller {
    
    public static Result index(String langKey) {
        Project p = new Project("test", "this is a text\n with line break", "test;other one;third", new DateTime(2012,6,1,1,1), new DateTime(2013,1,1,1,1), null, null, new ArrayList<ProjectType>());
        List<Project> list = new ArrayList<Project>();
        list.add(p);
        return ok(views.html.projects.render(list));
    }
    
    public static Result index2(String langKey,String type) {
        return TODO;
    }
    
    public static Result addProject() {
        return TODO;
    }
}
