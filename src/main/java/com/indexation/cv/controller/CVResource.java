package com.indexation.cv.controller;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.data.ErrorResponse;
import com.indexation.cv.data.Response;
import com.indexation.cv.exception.CVIndexationException;
import com.indexation.cv.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/cv")
public class CVResource {
    @Autowired
    private CVService cvService;

    @GetMapping
    public ResponseEntity<List<CVModel>> searchCv(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(cvService.searchCV(keyword));
    }

    /**
     * Create an index of the PDF
     * @param file
     * @return Response
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<CVModel> uploadCv(@RequestParam("file")MultipartFile file) throws CVIndexationException {
        try {
            String filename = file.getOriginalFilename();
            String[] fn = filename.split("\\.");
            String ext = fn[fn.length - 1];
            if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.PDF)) {
                String content = cvService.parsePdf(file);
                return ResponseEntity.status(HttpStatus.OK).body(cvService.saveCV(new CVModel(filename, DocumentType.PDF, content)));
            } else if (DocumentType.valueOf(ext.toUpperCase()).equals(DocumentType.DOC)) {
                throw new CVIndexationException("TODO");
            } else {
                throw new CVIndexationException("Extension not allowed: "+ext);
            }
        } catch (IOException | NullPointerException | IllegalArgumentException e) {
            throw new CVIndexationException(e.getMessage());
        }
    }
}
