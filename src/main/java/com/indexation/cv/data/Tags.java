package com.indexation.cv.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "tags")
public class Tags {
    @Id
    private String id;
    private final String cvId;
    private List<String> tags;

    public Tags(String cvId, List<String> tags) {
        this.cvId=cvId;
        this.tags=tags;
    }

    public String getId() { return id; }
    public String getCvId() { return cvId; }
    public List<String> getTags() {
        return tags;
    }
}
