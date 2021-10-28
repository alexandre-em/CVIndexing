package com.indexation.cv.controller;

import com.indexation.cv.Constant;
import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.data.Error;
import com.indexation.cv.service.CVLogger;
import com.indexation.cv.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/cv")
@Tag(name = "CV",description = "Search and upload CVs")
public class CVResource {
    @Autowired
    private CVService cvService;

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
    @ApiResponse(responseCode = "415", description = "Unsupported Media type. Only accept: *.doc, *.docx or *.pdf", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))})
    @ApiResponse(responseCode = "500", description = "Internal error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Error.class))})
    @PostMapping
    public ResponseEntity<CVModel> uploadCv(@RequestParam("file")MultipartFile file) {
        CVLogger.info("[POST] /api/v1/cv : Entering into uploadCv");
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String filename = timeStamp+"_"+file.getOriginalFilename();
            String newPath = "./static/"+filename;
            String[] fn = filename.split("\\.");
            String ext = fn[fn.length - 1];
            File cvFile = CVService.convertMultipartFile(file, newPath);
            if (DocumentType.PDF.name().equals(ext.toUpperCase())) {
                CVLogger.info("uploadCv: Parsing pdf file...");
                String content = cvService.parsePdf(cvFile);
                CVModel cv =  new CVModel(filename, DocumentType.PDF, Constant.API_URL + "/static/" + filename, content, new Date().getTime()+"");
                CVLogger.info("uploadCv: Saving pdf file data...");
                return ResponseEntity.status(HttpStatus.CREATED).body(cvService.saveCV(cv));
            } else {
                if (DocumentType.DOC.name().equals(ext.toUpperCase()) || DocumentType.DOCX.name().equals(ext.toUpperCase())) {
                    CVLogger.info("uploadCv: Parsing word file...");
                    String content;
                    DocumentType type;
                    if (ext.toUpperCase(Locale.ROOT).equals(DocumentType.DOC.name())) {
                        content = cvService.parseDoc(cvFile);
                        type = DocumentType.DOC;
                    } else {
                        content = cvService.parseDocX(cvFile);
                        type = DocumentType.DOCX;
                    }
                    CVModel cv = new CVModel(filename, type, Constant.API_URL + "/static/" + filename, content, new Date().getTime() + "");
                    CVLogger.info("uploadCv: Saving word file data...");
                    return ResponseEntity.status(HttpStatus.CREATED).body(cvService.saveCV(cv));
                }
                CVLogger.error("uploadCv: File extension not allowed");
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
            }
        } catch (IOException | NullPointerException | InvalidFormatException e) {
            CVLogger.error("uploadCv: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
