package models.forms;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingType;

import org.joda.time.DateTime;

import ch.qos.logback.core.joran.spi.DefaultClass;

import play.data.validation.Constraints.Required;

public class ProjectFormData {
    
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
    
    
    
    private Boolean isApplication;
    
    private Boolean isWeb;
    
    private Boolean isGame;
    
    public ProjectFormData() {
        
    }

    public ProjectFormData(String title_de, String description_de, String title_en, String description_en, String devStart, String devEnd, String technologiesString, File image, File upload,
            Boolean isApplication, Boolean isWeb, Boolean isGame) {
        super();
        this.title_de = title_de;
        this.description_de = description_de;
        this.title_en = title_en;
        this.description_en = description_en;
        this.devStart = devStart;
        this.devEnd = devEnd;
        this.technologiesString = technologiesString;
        this.image = image;
        this.upload = upload;
        this.isApplication = isApplication;
        this.isWeb = isWeb;
        this.isGame = isGame;
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
