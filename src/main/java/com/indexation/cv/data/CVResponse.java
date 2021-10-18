package com.indexation.cv.data;

public class CVResponse extends CVModel {

    public CVResponse(String filename, DocumentType type, String url, String content) {
       super(filename, type, url, content);
    }

    public String getFilename() { return super.filename; }
    public String getType() { return super.type.toString(); }
    public String getContent() { return super.content; }
    public String getUrl(){ return super.url; }
}
