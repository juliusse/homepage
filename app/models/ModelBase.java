package models;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonProperty;

import service.database.CouchDBDatabaseService;

public abstract class ModelBase {

    @JsonProperty("_id")
    private String id;
    
    @JsonProperty("_rev")
    private String revision;
    
    @JsonProperty("_attachments")
    private JsonNode attachments;
    
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
    
    
    public JsonNode getAttachments() {
        return attachments;
    }

    public void setAttachments(JsonNode attachments) {
        this.attachments = attachments;
    }

    public void save() {
        if(id == null) {
            this.id = type+"_"+System.currentTimeMillis();
            CouchDBDatabaseService.createDocument(this);
        } else {
            CouchDBDatabaseService.updateDocument(this);
        }
        
        JsonNode node = CouchDBDatabaseService.getById(JsonNode.class, this.id);
        this.revision = node.get("_rev").asText();
    }
    
    
}
