package com.indexation.cv.controller;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.ErrorResponse;
import com.indexation.cv.service.CVService;
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

    @GetMapping
    public ResponseEntity<List<CVModel>> searchCv(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(cvService.searchCV(keyword));
    }

    @PostMapping
    public ResponseEntity<ErrorResponse> uploadCv(@RequestParam("file")MultipartFile file) throws IOException {
        String content = cvService.parsePdf(file);
        return  ResponseEntity.status(HttpStatus.OK).body(new ErrorResponse(content));
    }
}
