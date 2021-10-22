package com.indexation.cv.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "cv")
public class CVModel {
    @Id
    private String id; // public id
    protected String filename;
    protected DocumentType type;
    protected String content;
    protected String url;
    private final String uploadedDate;


    public CVModel(String filename, DocumentType type, String url, String content, String uploadedDate) {
        this.filename=filename;
        this.type=type;
        this.content=content;
        this.url=url;
        this.uploadedDate=uploadedDate;
    }

    public String getId(){ return this.id; }
    public String getType(){ return this.type.toString(); }
    public String getUploadedDate(){ return this.uploadedDate; }
}
