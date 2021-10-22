package com.indexation.cv.data;

public class CVResponse extends CVModel {

    public CVResponse(String filename, DocumentType type, String url, String content, String uploadedDate) {
       super(filename, type, url, content, uploadedDate);
    }

    public String getFilename() { return super.filename; }
    public String getContent() { return super.content; }
    public String getUrl(){ return super.url; }
}
