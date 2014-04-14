package info.seltenheim.homepage.controllers;

import info.seltenheim.homepage.comparators.DateRangeComparators;
import info.seltenheim.homepage.controllers.secured.OnlyLoggedIn;
import info.seltenheim.homepage.controllers.utils.ImageUtils;
import info.seltenheim.homepage.models.forms.ProjectFormData;
import info.seltenheim.homepage.services.filesystem.FileSystemService;
import info.seltenheim.homepage.services.projects.Project;
import info.seltenheim.homepage.services.projects.Project.ProjectType;
import info.seltenheim.homepage.services.projects.ProjectsService;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Component
public class ProjectsController extends Controller {
    private final int maxImageWidth = Play.application().configuration().getInt("controllers.ProjectsController.images.maxWidth");

    @Autowired
    private ProjectsService projectsService;

    @Autowired
    private FileSystemService fileSystemService;

    public static final Form<ProjectFormData> projectForm = Form.form(ProjectFormData.class);

    public Result index(String langKey, String type) throws IOException {
        if (!type.equals("")) {
            // solution while filtered pages are disabled
            return redirect(routes.ProjectsController.index(langKey, ""));
        }

        List<Project> projects = null;
        if (!type.isEmpty()) {
            projects = projectsService.findProjectsOfType(ProjectType.valueOf(type));
        } else {
            projects = projectsService.findAllProjects();
        }
        Collections.sort(projects, DateRangeComparators::compareEndDateTimes);
        Logger.debug("number of Projects: " + projects.size());
        return ok(info.seltenheim.homepage.views.html.projects.render(projects));
    }

    // GET
    public Result renderAdd(String langKey) {
        return ok(info.seltenheim.homepage.views.html.projectAdd.render(projectForm));
    }

    // GET
    @Security.Authenticated(OnlyLoggedIn.class)
    public Result renderEdit(String langKey, String projectId) throws IOException {
        return ok(info.seltenheim.homepage.views.html.projectAdd.render(projectForm.fill(new ProjectFormData(projectsService.findProjectById(projectId)))));
    }

//    @TrackIgnore
    public Result getImage(String projectId) throws IOException {
        final String referer = request().getHeader("Referer");
        if (Play.isProd() && referer != null && !referer.contains("seltenheim") && !referer.contains("js")) {
            return ok(Play.application().getFile("public/images/julius_seltenheim_logo.png"));
        }
        final Project project = projectsService.findProjectById(projectId);
        File image = null;
        try {
            image = fileSystemService.getImageAsFile(project.getMainImagePath());
        } catch (FileNotFoundException e) {
            Logger.error("Image for project '" + projectId + "' not found!");
            return notFound();
        }
        String hash = null;
        InputStream in = null;

        try {
            in = new FileInputStream(image);
            hash = DigestUtils.sha256Hex(in);
        } finally {
            IOUtils.closeQuietly(in);
        }

        final String oldEtag = request().getHeader("If-None-Match");
        if (oldEtag != null && oldEtag.equals(hash)) {
            return status(NOT_MODIFIED);
        }

        response().setContentType("image/png");
        response().setHeader("Etag", hash);

        return ok(image);
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
                project = projectsService.upsertProject(project);
            } else {
                updateProject(data.getId(), project);
            }

            return redirect(routes.ProjectsController.index(lang().language(), ""));
        }

    }

    private byte[] getScaledImage() throws IOException {

        ByteArrayOutputStream out = null;
        File imageFile = null;
        try {
            imageFile = Controller.request().body().asMultipartFormData().getFile("image").getFile();
        } catch (NullPointerException e) {
            Logger.error("error scaling image!", e);
            return null;
        }
        byte[] output = null;

        try {
            out = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(imageFile);
            if (image != null) {

                if (image.getWidth() > 250) {
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
        final Project project = projectsService.findProjectById(projectId);
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

        projectsService.upsertProject(project);
    }

}