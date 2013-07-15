package models.forms;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import play.data.validation.Constraints.Required;
import services.database.Project;
import services.database.Project.ProjectType;

import com.google.common.base.Joiner;

public class ProjectFormData {

    private String id;
    @Required
    private String title_de;
    @Required
    private String description_de;
    @Required
    private String title_en;
    @Required
    private String description_en;

    @Required
    private String devStart;

    private String devEnd;

    @Required
    private String technologiesString;

    private File image;

    private File upload;

    private Boolean displayOnFrontpage;

    private Boolean isApplication;

    private Boolean isWeb;

    private Boolean isGame;

    public ProjectFormData() {

    }

    public ProjectFormData(Project project) {
        this.description_de = project.getDescription("de");
        this.description_en = project.getDescription("en");
        this.title_de = project.getTitle("de");
        this.title_en = project.getTitle("en");
        this.devEnd = project.getDevelopmentEndString();
        this.devStart = project.getDevelopmentStartString();
        this.displayOnFrontpage = project.getDisplayOnStartPage();
        this.id = project.getId();
        this.isApplication = project.getTypeOf().contains(ProjectType.Application);
        this.isWeb = project.getTypeOf().contains(ProjectType.Website);
        this.isGame = project.getTypeOf().contains(ProjectType.Game);
        this.technologiesString = Joiner.on(";").join(project.getTechnologies());
    }

    public Project toProject() {
        final Map<String, String> titleMap = new HashMap<String, String>();
        titleMap.put("de", title_de);
        titleMap.put("en", title_en);
        final Map<String, String> descriptionMap = new HashMap<String, String>();
        descriptionMap.put("de", description_de);
        descriptionMap.put("en", description_en);
        final boolean displayOnStartPage = displayOnFrontpage != null;
        final List<String> technologyList = Arrays.asList(technologiesString.split(";"));
        final DateTime developmentStart = Project.PROJECT_DATETIME_FORMAT.parseDateTime(devStart);
        final DateTime developmentEnd = devEnd.isEmpty() ? null : Project.PROJECT_DATETIME_FORMAT.parseDateTime(devEnd);
        final String mainImagePath = null;
        final String filePath = null;
        final List<ProjectType> typeOf = new ArrayList<Project.ProjectType>();
        if (isWeb != null)
            typeOf.add(ProjectType.Website);
        if (isGame != null)
            typeOf.add(ProjectType.Game);
        if (isApplication != null)
            typeOf.add(ProjectType.Application);

        return new Project(titleMap, descriptionMap, displayOnStartPage, technologyList, developmentStart, developmentEnd, mainImagePath, filePath, typeOf);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDisplayOnFrontpage() {
        return displayOnFrontpage;
    }

    public void setDisplayOnFrontpage(Boolean displayOnFrontpage) {
        this.displayOnFrontpage = displayOnFrontpage;
    }

    public String getTitle_de() {
        return title_de;
    }

    public void setTitle_de(String title_de) {
        this.title_de = title_de;
    }

    public String getDescription_de() {
        return description_de;
    }

    public void setDescription_de(String description_de) {
        this.description_de = description_de;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDevStart() {
        return devStart;
    }

    public void setDevStart(String devStart) {
        this.devStart = devStart;
    }

    public String getDevEnd() {
        return devEnd;
    }

    public void setDevEnd(String devEnd) {
        this.devEnd = devEnd;
    }

    public String getTechnologiesString() {
        return technologiesString;
    }

    public void setTechnologiesString(String technologiesString) {
        this.technologiesString = technologiesString;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public Boolean getIsApplication() {
        return isApplication;
    }

    public void setIsApplication(Boolean isApplication) {
        this.isApplication = isApplication;
    }

    public Boolean getIsWeb() {
        return isWeb;
    }

    public void setIsWeb(Boolean isWeb) {
        this.isWeb = isWeb;
    }

    public Boolean getIsGame() {
        return isGame;
    }

    public void setIsGame(Boolean isGame) {
        this.isGame = isGame;
    }
}
