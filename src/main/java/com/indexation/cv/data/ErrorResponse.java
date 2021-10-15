package com.indexation.cv.data;

public class ErrorResponse implements Response {
    private String message;
    public ErrorResponse(String message) {
        this.message=message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message=message; }
}
