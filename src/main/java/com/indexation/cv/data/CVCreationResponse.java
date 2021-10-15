package com.indexation.cv.data;

public class CVCreationResponse implements Response {
    private String id;
    private String message;
    public CVCreationResponse(String id, String message) {
        this.id=id;
        this.message=message;
    }
    public String getId() { return id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message=message; }
}
