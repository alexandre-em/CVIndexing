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


    public CVModel(String filename, DocumentType type, String url, String content) {
        this.filename=filename;
        this.type=type;
        this.content=content;
        this.url=url;
    }

    public String getId(){ return this.id; }
}
