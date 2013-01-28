package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.joda.time.DateTime;

@Entity
public class Project {
    
    @Id
    protected Long id;
    
    protected String title;
    protected String description;
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
        
    }

    public Project(String title, String description, String technologies, DateTime developmentStart, DateTime developmentEnd, byte[] mainImage, String downloadLink, List<ProjectType> typeOf) {
        super();
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.developmentStart = developmentStart;
        this.developmentEnd = developmentEnd;
        this.mainImage = mainImage;
        this.downloadLink = downloadLink;
        this.typeOf = typeOf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
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

    public byte[] getMainImage() {
        return mainImage;
    }

    public void setMainImage(byte[] mainImage) {
        this.mainImage = mainImage;
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
    
    
}
