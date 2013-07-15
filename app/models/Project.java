package models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.api.templates.Html;
import controllers.Application;

public class Project {
    public enum ProjectType {Application, Website, Game};

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM");

    private String id;

    private String titleDe;
    private String titleEn;
    private String descriptionDe;
    private String descriptionEn;

    private Boolean displayOnStartPage;

    private List<String> technologies;
    private DateTime developmentStart;
    private DateTime developmentEnd;

    private String mainImagePath;

    private String filePath;
    private List<ProjectType> typeOf;

    
    public Project() {
        typeOf = new ArrayList<ProjectType>();
        technologies = new ArrayList<String>();
    }

//    public static Project createFromRequest(ProjectFormData data) throws IOException {
//        Project project = null;
//        if(data.getId().isEmpty()) {
//            project = new Project();
//        } else {
//            project = Project.findById(data.getId());
//        }
//        project.setTitleDe(data.getTitle_de());
//        project.setTitleEn(data.getTitle_en());
//        project.setDescriptionDe(data.getDescription_de());
//        project.setDescriptionEn(data.getDescription_en());
//
//        DateTime fromDate = dateTimeFormatter.parseDateTime(data.getDevStart());
//        project.setDevelopmentStart(fromDate.plusWeeks(1));
//
//        if(data.getDevEnd() != null && !data.getDevEnd().isEmpty()) {
//            project.setDevelopmentEnd(dateTimeFormatter.parseDateTime(data.getDevEnd()).plusWeeks(1));
//        }
//
//        //technologies
//        String[] technologies = data.getTechnologiesString().split(";");
//        List<String> tecList = new ArrayList<String>();
//        for(String tec : technologies) {
//            tecList.add(tec.trim());
//        }
//        Collections.sort(tecList);
//        project.setTechnologies(tecList);
//
//        //display on front page
//        if(data.getDisplayOnFrontpage() != null) {
//            project.setDisplayOnStartPage(true);
//        }
//
//        List<ProjectType> types = new ArrayList<ProjectType>();
//        if(data.getIsApplication() != null) {
//            types.add(ProjectType.Application);
//        }
//        if(data.getIsWeb() != null) {
//            types.add(ProjectType.Website);
//        }
//        if(data.getIsGame() != null) {
//            types.add(ProjectType.Game);
//        }
//
//        project.setTypeOf(types);
//        File imageFile = Controller.request().body().asMultipartFormData().getFile("image").getFile();
//
//        BufferedImage image =  ImageIO.read(imageFile);
//        if(image != null) {
//            if(image.getWidth() > 250) {
//                image = scaleImageKeepRelations(image, 250);
//            }
//            File f = File.createTempFile("mainImage", "png");
//            ImageIO.write(image, "png", f);
//            //project.setMainImage(imageToBase64String(f));
//            project.setMainImage(f);
//        }
//
//        //TODO application file
//        project.save();
//
//        return project;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * 
     * @return title for current language, default is english
     */
    @JsonIgnore
    public String getTitle() {
        String lang = Application.getSessionLang();
        if(lang.equals("de")) {
            return titleDe;
        } else {
            return titleEn;
        }
    }

    @JsonIgnore
    public Html getTitleAsHtml() {

        return Html.apply(getTitle().replace("\n", "<br>"));
    }



    //    public Map<String, String> getTitleLangMap() {
    //        return titleLangMap;
    //    }
    //
    //    public void setTitleForLang(String lang, String title) {
    //        this.titleLangMap.put(lang, title);
    //    }
    //    
    //    public void removeTitleForLang(String lang) {
    //        this.titleLangMap.remove(lang);
    //    }

    public String getTitleDe() {
        return titleDe;
    }

    public void setTitleDe(String titleDe) {
        this.titleDe = titleDe;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDescriptionDe() {
        return descriptionDe;
    }

    public void setDescriptionDe(String descriptionDe) {
        this.descriptionDe = descriptionDe;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    /**
     * 
     * @return description for current language, default is english
     */
    @JsonIgnore
    public String getDescription() {
        String lang = Application.getSessionLang();
        if(lang.equals("de")) {
            return descriptionDe;
        } else {
            return descriptionEn;
        }
    }

    @JsonIgnore
    public Html getDescriptionAsHtml() {
        return Html.apply(getDescription().replace("\n", "<br>"));

    }

    //    public void setDescriptionForLang(String lang, String description) {
    //        this.descriptionLangMap.put(lang, description);
    //    }
    //    
    //    public void removeDescriptionForLang(String lang) {
    //        this.descriptionLangMap.remove(lang);
    //    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public DateTime getDevelopmentStart() {
        return developmentStart;
    }

    @JsonIgnore
    public String getDevelopmentStartString() {
        return dateTimeFormatter.print(developmentStart);
    }

    public void setDevelopmentStart(DateTime developmentStart) {
        this.developmentStart = developmentStart;
    }

    public DateTime getDevelopmentEnd() {
        return developmentEnd;
    }

    @JsonIgnore
    public String getDevelopmentEndString() {
        if(developmentEnd != null)
            return dateTimeFormatter.print(developmentEnd);
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


}
