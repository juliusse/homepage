package models;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import models.forms.ProjectFormData;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.api.templates.Html;
import play.db.ebean.Model;
import play.mvc.Controller;
import controllers.Application;

@Entity
public class Project extends Model {
    private static final Model.Finder<Long,Project> find = new Model.Finder<Long,Project>(Long.class, Project.class);
    @Id
    protected Long id;

    protected String titleDe;
    protected String titleEn;
    protected String descriptionDe;
    protected String descriptionEn;
    //    protected HashMap<String,String> titleLangMap;
    //    protected HashMap<String,String> descriptionLangMap;
    protected String technologies;
    protected DateTime developmentStart;
    protected DateTime developmentEnd;

    //@Lob
    @Column(columnDefinition="bytea")
    protected byte[] mainImage;

    protected String downloadLink;

    @ManyToMany
    protected List<ProjectType> typeOf;

    public Project() {
        //        titleLangMap = new HashMap<String, String>();
        //        descriptionLangMap = new HashMap<String, String>();
        typeOf = new ArrayList<ProjectType>();
    }

    public Project(String deTitle, String deDescription, String enTitle, String enDescription, List<String> technologies, DateTime developmentStart, DateTime developmentEnd, byte[] mainImage, String downloadLink, List<ProjectType> typeOf) {
        this();
        //        titleLangMap.put("de", deTitle);
        //        titleLangMap.put("en", enTitle);
        //        descriptionLangMap.put("de", deDescription);
        //        descriptionLangMap.put("en", enDescription);
        this.titleDe = deTitle;
        this.titleEn = enTitle;
        this.descriptionDe = deDescription;
        this.descriptionEn = enDescription;
        this.setTechnologies(technologies);
        this.developmentStart = developmentStart;
        this.developmentEnd = developmentEnd;
        this.mainImage = mainImage;
        this.downloadLink = downloadLink;
        this.typeOf = typeOf;
    }

    public static Project findById(Long id) {
        return find.byId(id);
    }

    public static List<Project> findAll() {
        return find.all();
    }

    public static Project createFromRequest(ProjectFormData data) throws IOException {
        Project project = new Project();
        project.setTitleDe(data.getTitle_de());
        project.setTitleEn(data.getTitle_en());
        project.setDescriptionDe(data.getDescription_de());
        project.setDescriptionEn(data.getDescription_en());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        DateTime fromDate = formatter.parseDateTime(data.getDevStart());
        project.setDevelopmentStart(fromDate);

        if(data.getDevEnd() != null) {
            project.setDevelopmentEnd(formatter.parseDateTime(data.getDevEnd()));
        }

        //technologies
        String[] technologies = data.getTechnologiesString().split(";");
        List<String> tecList = new ArrayList<String>();
        for(String tec : technologies) {
            tecList.add(tec.trim());
        }
        Collections.sort(tecList);
        project.setTechnologies(tecList);

        List<ProjectType> types = new ArrayList<ProjectType>();
        if(data.getIsApplication() != null) {
            types.add(ProjectType.application());
        }
        if(data.getIsWeb() != null) {
            types.add(ProjectType.web());
        }
        if(data.getIsApplication() != null) {
            types.add(ProjectType.game());
        }

        project.setTypeOf(types);
        File imageFile = Controller.request().body().asMultipartFormData().getFile("image").getFile();
        BufferedImage image =  ImageIO.read(imageFile);
        if(image.getWidth() > 250) {
            image = scaleImageKeepRelations(image, 250);
        }
        File f = File.createTempFile(project.getTitle(), "jpg");
        ImageIO.write(image, "jpg", f);
        project.setMainImage(f);

        //TODO application file
        project.save();

        return project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return title for current language, default is english
     */
    public String getTitle() {
        String lang = Application.getSessionLang();
        if(lang.equals("de")) {
            return titleDe;
            //            String title = titleLangMap.get("en");
            //            Logger.warn("lang "+lang+" not found. Using en: "+title);
            //            return title;
        } else {
            return titleEn;
        }
    }

    public Html getTitleAsHtml() {

        return Html.apply(getTitle());
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
    public String getDescription() {
        String lang = Application.getSessionLang();
        if(lang.equals("de")) {
            return descriptionDe;
        } else {
            return descriptionEn;
        }
    }

    public Html getDescriptionAsHtml() {
        return Html.apply(getDescription());

    }

    //    public void setDescriptionForLang(String lang, String description) {
    //        this.descriptionLangMap.put(lang, description);
    //    }
    //    
    //    public void removeDescriptionForLang(String lang) {
    //        this.descriptionLangMap.remove(lang);
    //    }

    public List<String> getTechnologies() {
        
        return Arrays.asList(technologies.split(";"));
    }

    public void setTechnologies(List<String> technologies) {
        String tecString = "";
        for(int i = 0; i < technologies.size(); i++) {
            tecString += technologies.get(i);
            if(i + 1 < technologies.size())
                tecString+=";";
        }
        this.technologies = tecString;
    }

    public DateTime getDevelopmentStart() {
        return developmentStart;
    }

    public void setDevelopmentStart(DateTime developmentStart) {
        this.developmentStart = developmentStart;
    }

    public DateTime getDevelopmentEnd() {
        return developmentEnd;
    }

    public void setDevelopmentEnd(DateTime developmentEnd) {
        this.developmentEnd = developmentEnd;
    }

    public File getMainImage() {
        try {
            File f = File.createTempFile(this.getTitle(), "jpg");
            FileUtils.writeByteArrayToFile(f, this.mainImage);
            return f;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setMainImage(File image) {
        try {
            this.mainImage = FileUtils.readFileToByteArray(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public List<ProjectType> getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(List<ProjectType> typeOf) {
        this.typeOf = typeOf;
    }

    private  static BufferedImage scaleImageKeepRelations(BufferedImage img,int newWidth) {

        int oriWidth = img.getWidth();
        int oriHeight = img.getHeight();

        double scale = newWidth / (float) oriWidth;
        int newHeight = (int)(oriHeight * scale);

        BufferedImage fImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        AffineTransform at = new AffineTransform();
        at.scale(newWidth / (double)oriWidth, newHeight / (double)oriHeight);

        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        op.filter(img, fImage);

        return fImage;
    }
}
