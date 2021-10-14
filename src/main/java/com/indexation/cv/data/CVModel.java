package com.indexation.cv.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "cv")
public class CVModel {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    // TODO: experience
    // TODO: education

    public String getId(){ return this.id; }
    public String getFirstName(){ return this.firstName; }
    public void setFirstName(String firstName){ this.firstName=firstName; }
    public String getLastName(){ return this.lastName; }
    public void setLastName(String lastName){ this.lastName=lastName; }
}
