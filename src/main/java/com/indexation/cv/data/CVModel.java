package com.indexation.cv.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "cv")
public class CVModel {
    @Id
    private String id; // public id
    private String filename;
    private DocumentType type;
    private String content;

    public CVModel(String filename, DocumentType type, String content) {
        this.filename=filename;
        this.type=type;
        this.content=content;
    }

    public String getId(){ return this.id; }
}
