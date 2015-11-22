package info.seltenheim.homepage.services.projects;

import info.seltenheim.homepage.services.DateRangeModel;
import info.seltenheim.homepage.services.PersistentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.mvc.Controller;
import play.twirl.api.Html;

public class Project implements PersistentModel, DateRangeModel {
    public enum ProjectType {
        Application, Website, Game
    };

    public static final DateTimeFormatter PROJECT_DATETIME_FORMAT = DateTimeFormat.forPattern("yyyy-MM");

    private String id;

    private Map<String, String> titleMap;
    private Map<String, String> descriptionMap;

    private Boolean displayOnStartPage;

    private List<String> technologyList;
    private DateTime developmentStart;
    private DateTime developmentEnd;

    private String mainImagePath;

    private String filePath;
    private List<ProjectType> typeOf;

    public Project() {
        id = null;
        typeOf = new ArrayList<ProjectType>();
        technologyList = new ArrayList<String>();
        titleMap = new HashMap<String, String>();
        descriptionMap = new HashMap<String, String>();
    }

    public Project(Map<String, String> titleMap, Map<String, String> descriptionMap, Boolean displayOnStartPage, List<String> technologyList, DateTime developmentStart, DateTime developmentEnd,
            String mainImagePath, String filePath, List<ProjectType> typeOf) {
        super();
        this.id = null;
        this.titleMap = titleMap;
        this.descriptionMap = descriptionMap;
        this.displayOnStartPage = displayOnStartPage;
        this.technologyList = technologyList;
        this.developmentStart = developmentStart;
        this.developmentEnd = developmentEnd;
        this.mainImagePath = mainImagePath;
        this.filePath = filePath;
        this.typeOf = typeOf;
    }

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * @return title for current language, default is english
     */
    public String getTitle() {
        String lang = Controller.lang().language();
        if (titleMap.containsKey(lang))
            return titleMap.get(lang);
        else
            return null;
    }

    Map<String, String> getTitleMap() {
        return titleMap;
    }

    void setTitleMap(Map<String, String> titleMap) {
        this.titleMap = titleMap;
    }

    Map<String, String> getDescriptionMap() {
        return descriptionMap;
    }

    void setDescriptionMap(Map<String, String> descriptionMap) {
        this.descriptionMap = descriptionMap;
    }

    public String getTitle(final String langKey) {
        return titleMap.get(langKey);
    }

    public void setTitle(final String langKey, final String title) {
        titleMap.put(langKey, title);
    }

    public play.twirl.api.Html getTitleAsHtml() {

        return play.twirl.api.Html.apply(getTitle().replace("\n", "<br>"));
    }

    /**
     * 
     * @return description for current language, default is english
     */
    public String getDescription() {
        String lang = Controller.lang().language();
        if (descriptionMap.containsKey(lang)) {
            return descriptionMap.get(lang);
        } else {
            return null;
        }
    }

    public String getDescription(final String langKey) {
        return descriptionMap.get(langKey);
    }

    public void setDescription(final String langKey, final String description) {
        descriptionMap.put(langKey, description);
    }

    public Html getDescriptionAsHtml() {
        return Html.apply(getDescription().replace("\n", "<br>"));
    }

    public List<String> getTechnologies() {
        return technologyList;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologyList = technologies;
    }

    public DateTime getDevelopmentStart() {
        return developmentStart;
    }

    public String getDevelopmentStartString() {
        return PROJECT_DATETIME_FORMAT.print(developmentStart);
    }

    public void setDevelopmentStart(DateTime developmentStart) {
        this.developmentStart = developmentStart;
    }

    public DateTime getDevelopmentEnd() {
        return developmentEnd;
    }

    public String getDevelopmentEndString() {
        if (developmentEnd != null)
            return PROJECT_DATETIME_FORMAT.print(developmentEnd);
        else
            return "";

    }

    public void setDevelopmentEnd(DateTime developmentEnd) {
        this.developmentEnd = developmentEnd;
    }

    public List<ProjectType> getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(List<ProjectType> typeOf) {
        this.typeOf = typeOf;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getDisplayOnStartPage() {
        return displayOnStartPage;
    }

    public void setDisplayOnStartPage(Boolean displayOnStartPage) {
        this.displayOnStartPage = displayOnStartPage;
    }

    @Override
    public DateTime getFromDate() {
        return getDevelopmentStart();
    }

    @Override
    public DateTime getToDate() {
        return getDevelopmentEnd();
    }
}
