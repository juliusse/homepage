package models;

import org.codehaus.jackson.annotate.JsonProperty;

import service.database.CouchDBDatabaseService;

public abstract class ModelBase {

    @JsonProperty("_id")
    private String id;
    
    @JsonProperty("_rev")
    private String revision;
    
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public void save() {
        CouchDBDatabaseService.saveDocument(this);
    }
}
