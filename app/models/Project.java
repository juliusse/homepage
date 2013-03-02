package models;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import models.forms.ProjectFormData;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import play.Logger;
import play.api.templates.Html;
import play.mvc.Controller;
import service.database.CouchDBDatabaseService;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import controllers.Application;

public class Project extends ModelBase implements Serializable {
    public enum ProjectType {Application, Website, Game};

    protected String titleDe;
    protected String titleEn;
    protected String descriptionDe;
    protected String descriptionEn;

    protected List<String> technologies;
    protected DateTime developmentStart;
    protected DateTime developmentEnd;

    //base64 string
    protected String mainImage;

    //base64 file
    protected String file;


    protected List<ProjectType> typeOf;

    public Project() {
        typeOf = new ArrayList<ProjectType>();
        setType("project");
    }

    public static List<Project> findAll() {
        return CouchDBDatabaseService.getAllProjects();
    }

    public static Project findById(String id) {
        return CouchDBDatabaseService.getById(Project.class, id);
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
            types.add(ProjectType.Application);
        }
        if(data.getIsWeb() != null) {
            types.add(ProjectType.Website);
        }
        if(data.getIsGame() != null) {
            types.add(ProjectType.Game);
        }

        project.setTypeOf(types);
        File imageFile = Controller.request().body().asMultipartFormData().getFile("image").getFile();
        BufferedImage image =  ImageIO.read(imageFile);
        if(image.getWidth() > 250) {
            image = scaleImageKeepRelations(image, 250);
        }
        File f = File.createTempFile(project.getTitle(), "jpg");
        ImageIO.write(image, "jpg", f);
        project.setMainImage(imageToBase64String(f));

        //TODO application file
        project.save();

        return project;
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
            //            String title = titleLangMap.get("en");
            //            Logger.warn("lang "+lang+" not found. Using en: "+title);
            //            return title;
        } else {
            return titleEn;
        }
    }

    @JsonIgnore
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

        return technologies;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
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

    public List<ProjectType> getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(List<ProjectType> typeOf) {
        this.typeOf = typeOf;
    }

    public String getMainImage() {
        //if(mainImage.isEmpty()) return null;

        return this.mainImage;
//        try {
//            BASE64Decoder decoder = new BASE64Decoder();
//            return decoder.decodeBuffer(this.mainImage);
//        } catch (Exception e) {
//            Logger.error("could not save image",e);
//            return null;
//        }
    }
    
    @JsonIgnore
    public File getMainImageFile() {
        if(mainImage.isEmpty()) return null;

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(this.mainImage);
            File file = File.createTempFile(this.getId(), ".jpg");
            FileUtils.writeByteArrayToFile(file, bytes);
            return file;
        } catch (Exception e) {
            Logger.error("could not save image",e);
            return null;
        }
    }

    public void setMainImage(String mainImage) {
        this.mainImage = new String(mainImage);
    }

    private static String imageToBase64String(File image) {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            FileInputStream in = new FileInputStream(image);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            encoder.encode(in, out);
            return new String(out.toByteArray(),"UTF-8");
        } catch (Exception e) {
            Logger.error("could not save image",e);
            return null;
        }
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
