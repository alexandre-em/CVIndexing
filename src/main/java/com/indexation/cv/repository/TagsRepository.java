package com.indexation.cv.repository;

import com.indexation.cv.data.Tags;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TagsRepository extends ElasticsearchRepository<Tags, String> {
    @Query("{\"match\": {\"cvId\": \"?0\" }}")
    List<Tags> findTagsByCvId(String cvId);
    @Query("{\"match\": {\"tags\": \"?0\" }}")
    List<Tags> findTagsByKeyword(String keywords);
    List<Tags> findTagsByTags(List<String> tags);
}
