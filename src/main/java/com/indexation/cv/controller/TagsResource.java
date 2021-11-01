package com.indexation.cv.controller;

import com.indexation.cv.Exception.TagException;
import com.indexation.cv.data.Tags;
import com.indexation.cv.service.TagsService;
import com.indexation.cv.utils.CVLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@Tag(name = "Tags", description = "CV's tags")
public class TagsResource {
    @Autowired
    TagsService tagsService;

    @Operation(summary = "Get tags of a CV")
    @ApiResponse(responseCode = "200", description = "Tag Found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Tags.class))})
    @ApiResponse(responseCode = "404", description = "Tag id not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class))})
    @GetMapping("/{id}")
    public ResponseEntity<Tags> findTags(@PathVariable String id) {
        try {
            CVLogger.info("Searching tags: "+id);
            Tags tags = tagsService.getTagsById(id);
            CVLogger.info("Tags found: "+id);
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } catch (TagException e) {
            CVLogger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Get tags of a CV")
    @ApiResponse(responseCode = "200", description = "Tags found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Tags.class))})
    @ApiResponse(responseCode = "404", description = "CV id not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class))})
    @GetMapping("/cv/{id}")
    public ResponseEntity<Tags> findCvTags(@PathVariable String id) {
        try {
            CVLogger.info("Searching tags of cv: "+id);
            Tags tags = tagsService.getTagsByCvId(id);
            CVLogger.info("tags of cv found "+id);
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } catch (TagException e) {
            CVLogger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Get CVs that have tags matching one or all the keywords", description = "To request all cv that contains all keywords, add the `match_all` parameter to `true`. Keywords have to be separated with a **comma**")
    @ApiResponse(responseCode = "200", description = "Tags found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Tags.class))})
    @ApiResponse(responseCode = "404", description = "There is no CV matching the tags entered", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class))})
    @GetMapping
    public ResponseEntity<List<Tags>> findTagsByKeywords(@RequestParam("search") String search, @RequestParam(name = "match_all", required = false, defaultValue = "false") boolean matchAll) {
        try {
            CVLogger.info("Searching CV tags matching "+search);
            List<Tags> tags = tagsService.getTagsByKeywords(search, matchAll);
            CVLogger.info("CVs Found");
            return ResponseEntity.status(HttpStatus.OK).body(tags);
        } catch (TagException e) {
           CVLogger.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Add tags of a CV")
    @ApiResponse(responseCode = "200", description = "Tags added", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Tags.class))})
    @ApiResponse(responseCode = "404", description = "CV id not found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class))})
    @ApiResponse(responseCode = "500", description = "Internal error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Exception.class))})
    @PatchMapping("/cv/{id}")
    public ResponseEntity<Tags> addCvTags(@PathVariable String id, @RequestParam("tags") List<String> tags) {
        try {
            CVLogger.info("Updating tags of cv: "+id);
            Tags tag = tagsService.updateTagsByCvId(id, tags);
            CVLogger.info("CV updated: "+id);
            return ResponseEntity.status(HttpStatus.OK).body(tag);
        } catch (TagException e) {
            CVLogger.error("There is no cv matching this `cv_id`");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
