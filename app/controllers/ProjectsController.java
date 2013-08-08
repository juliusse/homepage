package controllers;

import info.seltenheim.play2.usertracking.actions.TrackIgnore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import models.forms.ProjectFormData;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.database.DatabaseService;
import services.database.Project;
import services.database.Project.ProjectType;
import services.filesystem.FileSystemService;
import controllers.secured.OnlyLoggedIn;
import controllers.utils.ImageUtils;

@Component
public class ProjectsController extends Controller {
    private final int maxImageWidth = Play.application().configuration().getInt("controllers.ProjectsController.images.maxWidth"); 

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private FileSystemService fileSystemService;

    public static final Form<ProjectFormData> projectForm = Form.form(ProjectFormData.class);

    public Result index(String langKey, String type) throws IOException {
        List<Project> projects = null;
        if(!type.isEmpty()) {
            projects = databaseService.findProjectsOfType(ProjectType.valueOf(type));
        } else {
            projects = databaseService.findAllProjects();
        }
        Collections.sort(projects, new Comparator<Project>() {

            @Override
            public int compare(Project o1, Project o2) {
                final DateTime p1End = o1.getDevelopmentEnd();
                final DateTime p2End = o2.getDevelopmentEnd();
                if (p1End == null && p2End == null) {
                    return 0;
                } else if (p1End == null) {
                    return -1;
                } else if (p2End == null) {
                    return 1;
                } else {
                    return -p1End.compareTo(p2End);
                }
            }
        });
        Logger.debug("number of Projects: " + projects.size());
        return ok(views.html.projects.render(projects));
    }

    // GET
    public Result renderAdd(String langKey) {
        return ok(views.html.projectAdd.render(projectForm));
    }

    // GET
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result renderEdit(String langKey, String projectId) throws IOException {
        return ok(views.html.projectAdd.render(projectForm.fill(new ProjectFormData(databaseService.findProjectById(projectId)))));
    }

    @TrackIgnore
    public Result getImage(String projectId) throws IOException {
        final Project project = databaseService.findProjectById(projectId);
        return ok(fileSystemService.getImage(project.getMainImagePath()));
    }

    // POST
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result changeProject() throws IOException {
        Form<ProjectFormData> filledForm = projectForm.bindFromRequest();

        if (filledForm.hasErrors()) {
            return badRequest(filledForm.errorsAsJson());
        } else {
            final ProjectFormData data = filledForm.get();
            Project project = data.toProject();

            // save image
            final byte[] imageBytes = getScaledImage();
            String imagePath = null;
            if (imageBytes != null) {
                imagePath = fileSystemService.saveImage(imageBytes, "png");
                project.setMainImagePath(imagePath);
            }

            // TODO save file

            if (data.getId().isEmpty()) { // insert
                project = databaseService.upsertProject(project);
            } else {
                updateProject(data.getId(), project);
            }

            return redirect(routes.ProjectsController.index(Application.getSessionLang(),""));
        }

    }

    private byte[] getScaledImage() throws IOException {

        ByteArrayOutputStream out = null;
        File imageFile = null;
        try {
            imageFile = Controller.request().body().asMultipartFormData().getFile("image").getFile();
        } catch (NullPointerException e) {
            Logger.error("error scaling image!",e);
            return null;
        }
        byte[] output = null;

        try {
            out = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(imageFile);
            if (image != null) {
                
                if(image.getWidth() > 250) {
                    image = ImageUtils.scaleImageKeepRelations(image, maxImageWidth);
                }
                ImageIO.write(image, "png", out);
                output = out.toByteArray();
            }
        } finally {
            IOUtils.closeQuietly(out);
        }

        return output;
    }

    private void updateProject(String projectId, Project newData) throws IOException {
        final Project project = databaseService.findProjectById(projectId);
        // title
        project.setTitle("de", newData.getTitle("de"));
        project.setTitle("en", newData.getTitle("en"));
        project.setDescription("de", newData.getDescription("de"));
        project.setDescription("en", newData.getDescription("en"));
        project.setDisplayOnStartPage(newData.getDisplayOnStartPage());
        project.setDevelopmentEnd(newData.getDevelopmentEnd());
        project.setDevelopmentStart(newData.getDevelopmentStart());
        project.setTechnologies(newData.getTechnologies());
        project.setTypeOf(newData.getTypeOf());
        if (newData.getMainImagePath() != null)
            project.setMainImagePath(newData.getMainImagePath());
        if (newData.getFilePath() != null)
            project.setMainImagePath(newData.getFilePath());
        
        databaseService.upsertProject(project);
    }

}