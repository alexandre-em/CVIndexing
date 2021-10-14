package com.indexation.cv.repository;

import com.indexation.cv.data.CVModel;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends ElasticsearchRepository<CVModel, String> {
    @Query("{\"bool\": {\"must\": [{\"fuzzy\": {\"title\": \"?0\"}}]}}")
    List<CVModel> search(String keyword);
}
