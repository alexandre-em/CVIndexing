package com.indexation.cv.repository;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends ElasticsearchRepository<CVModel, String> {
    @Query("{\"match\": {\"content\": \"?0\" }}")
    List<CVModel> search(String keyword);
    List<CVModel> findCVModelsByContent(List<String> keywords);
    @Query("{\"term\": {\"_id\": \"?0\" }}")
    CVResponse searchById(String id);
}
