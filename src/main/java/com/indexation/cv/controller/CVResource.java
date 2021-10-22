package com.indexation.cv.controller;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.exception.CVIndexationException;
import com.indexation.cv.service.CVLogger;
import com.indexation.cv.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class CVResource {
    @Autowired
    private CVService cvService;
    public final static String API_URL = "http://localhost:8080/";

    /**
     * GET /api/v1/cv : Search single/multiple keyword on the cv
     * @param keyword
     * @return id of CVs that contains a word matching at least one of the keyword
     */
    @Operation(summary = "Get a list of cv ids matching the keywords, each keywords must be separated with a comma")
    @ApiResponse(responseCode = "200", description = "The list of cv ids", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CVModel.class))})
    @GetMapping
    public ResponseEntity<List<CVModel>> searchCv(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(cvService.searchCV(keyword));
    }

    /**
     * POST /api/v1/cv : Create an index of the PDF
     * @param file
     * @return Id of the cv created
     * @throws IOException
     */
    @Operation(summary = "Create an index of the CV")
    @ApiResponse(responseCode = "201", description = "Cv indexed", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CVModel.class))})
    @PostMapping
    public ResponseEntity<CVModel> uploadCv(@RequestParam("file")MultipartFile file) throws CVIndexationException {
        CVLogger.info("[POST] /api/v1/cv : Entering into uploadCv");
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String filename = timeStamp+"_"+file.getOriginalFilename();
            String newPath = "./static/"+filename;
            String[] fn = filename.split("\\.");
            String ext = fn[fn.length - 1];
            if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.PDF)) {
                CVLogger.info("uploadCv: Parsing pdf file...");
                String content = cvService.parsePdf(file, newPath);
                CVModel cv =  new CVModel(filename, DocumentType.PDF, API_URL + "/static/" + filename, content, new Date().getTime()+"");
                CVLogger.info("uploadCv: Saving pdf file data...");
                return ResponseEntity.status(HttpStatus.CREATED).body(cvService.saveCV(cv));
            } else if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.DOC) || DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.DOCX)) {
                CVLogger.info("uploadCv: Parsing word file...");
                String content;
                DocumentType type;
                if(ext.toUpperCase(Locale.ROOT).equals("DOC")){
                    content = cvService.parseDoc(file, newPath);
                    type = DocumentType.DOC;
                }
                else{
                    content = cvService.parseDocX(file, newPath);
                    type = DocumentType.DOCX;
                }
                CVModel cv =  new CVModel(filename, type, API_URL + "/static/" + filename, content, new Date().getTime()+"");
                CVLogger.info("uploadCv: Saving word file data...");
                return ResponseEntity.status(HttpStatus.CREATED).body(cvService.saveCV(cv));
            } else {
                CVLogger.error("uploadCv: File extension not allowed");
                throw new CVIndexationException("Extension not allowed: "+ext);
            }
        } catch (IOException | NullPointerException | IllegalArgumentException | InvalidFormatException e) {
            CVLogger.error("uploadCv: "+e.getMessage());
            throw new CVIndexationException(e.getMessage());
        }
    }
}
