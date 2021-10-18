package com.indexation.cv.controller;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.exception.CVIndexationException;
import com.indexation.cv.service.CVLogger;
import com.indexation.cv.service.CVService;
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

    /**
     * GET /api/v1/cv : Search single/multiple keyword on the cv
     * @param keyword
     * @return id of CVs that contains a word matching at least one of the keyword
     */
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
                return ResponseEntity.status(HttpStatus.OK).body(cvService.saveCV(new CVModel(filename, DocumentType.PDF, "http://localhost:8080/static/"+filename, content)));
            } else if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.DOC)) {
                CVLogger.info("uploadCv: Parsing word file...");
                throw new CVIndexationException("TODO");
            } else {
                CVLogger.error("uploadCv: File extension not allowed");
                throw new CVIndexationException("Extension not allowed: "+ext);
            }
        } catch (IOException | NullPointerException | IllegalArgumentException e) {
            CVLogger.error("uploadCv: "+e.getMessage());
            throw new CVIndexationException(e.getMessage());
        }
    }
}
