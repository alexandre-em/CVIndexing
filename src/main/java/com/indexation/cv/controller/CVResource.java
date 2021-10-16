package com.indexation.cv.controller;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.exception.CVIndexationException;
import com.indexation.cv.service.CVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class CVResource {
    @Autowired
    private CVService cvService;

    private static final Logger logger = LoggerFactory.getLogger(CVResource.class);

    @GetMapping
    public ResponseEntity<List<CVModel>> searchCv(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(cvService.searchCV(keyword));
    }

    /**
     * POST /api/v1/cv : Create an index of the PDF
     *
     * @param file
     * @return Id of the cv created
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<CVModel> uploadCv(@RequestParam("file")MultipartFile file) throws CVIndexationException {
        logger.info("[POST] /api/v1/cv : Entering into uploadCv");
        try {
            String filename = file.getOriginalFilename();
            String[] fn = filename.split("\\.");
            String ext = fn[fn.length - 1];
            if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.PDF)) {
                logger.info("uploadCv: Parsing pdf file...");
                String content = cvService.parsePdf(file);
                return ResponseEntity.status(HttpStatus.OK).body(cvService.saveCV(new CVModel(filename, DocumentType.PDF, content)));
            } else if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.DOC)) {
                logger.info("uploadCv: Parsing word file...");
                throw new CVIndexationException("TODO");
            } else {
                logger.error("uploadCv: File extension not allowed");
                throw new CVIndexationException("Extension not allowed: "+ext);
            }
        } catch (IOException | NullPointerException | IllegalArgumentException e) {
            logger.error("uploadCv: "+e.getMessage());
            throw new CVIndexationException(e.getMessage());
        }
    }
}
