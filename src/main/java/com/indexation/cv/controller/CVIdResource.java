package com.indexation.cv.controller;

import com.indexation.cv.data.CVResponse;
import com.indexation.cv.service.CVLogger;
import com.indexation.cv.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cv/{id}")
public class CVIdResource {
    @Autowired
    private CVService cvService;

    /**
     * GET /api/v1/cv/{id}: Allow to access the content of a cv
     * @param id
     * @return the content, the url of the CV
     */
    @GetMapping
    public ResponseEntity<CVResponse> searchById(@PathVariable("id") String id) {
        CVLogger.info("[GET] CV ID "+id);
        return ResponseEntity.ok(cvService.searchById(id));
    }
}
