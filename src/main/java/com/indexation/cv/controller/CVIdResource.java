package com.indexation.cv.controller;

import com.indexation.cv.data.CVResponse;
import com.indexation.cv.service.CVLogger;
import com.indexation.cv.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Get all details of a CV and its content")
    @ApiResponse(responseCode = "201", description = "Cv indexed", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CVResponse.class))})
    @GetMapping
    public ResponseEntity<CVResponse> searchById(@PathVariable("id") String id) {
        CVLogger.info("[GET] CV ID "+id);
        return ResponseEntity.ok(cvService.searchById(id));
    }
}
