package com.indexation.cv.service;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.repository.CVRepository;
import com.indexation.cv.utils.CVLogger;
import com.indexation.cv.utils.CVUtils;
import com.indexation.cv.utils.Constant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class CVService {
    @Autowired
    private CVRepository cvRepository;
    @Autowired
    private Environment env;

    public CVResponse searchById(String id) { return cvRepository.searchById(id); }
    public CVModel save(CVModel cv) { return cvRepository.save(cv); }
    public void delete(CVModel cv) { cvRepository.delete(cv); }

    public List<CVModel> searchCV(String keyword, boolean matchAll) {
        if (!matchAll)
            return cvRepository.search(keyword);
        else {
            List<String> keywords = Arrays.asList(keyword.split(","));
            return cvRepository.findCVModelsByContent(keywords);
        }
    }

    public CVModel saveCvPDF(MultipartFile file) throws IOException {
        File cvFile = CVUtils.convertMultipartFile(file, env.getActiveProfiles());
        CVLogger.info("uploadCv: Parsing pdf file...", env.getActiveProfiles());
        String content = CVUtils.parsePdf(cvFile, env.getActiveProfiles());
        CVModel cv =  new CVModel(cvFile.getName(), DocumentType.PDF, Constant.API_URL + "/static/" + cvFile.getName(), content, new Date().getTime()+"");
        return cvRepository.save(cv);
    }

    public CVModel saveCvWord(MultipartFile file, String ext) throws IOException, InvalidFormatException {
        File cvFile = CVUtils.convertMultipartFile(file, env.getActiveProfiles());
        CVLogger.info("uploadCv: Parsing word file...", env.getActiveProfiles());
        String content;
        DocumentType type;
        if (ext.toUpperCase(Locale.ROOT).equals(DocumentType.DOC.name())) {
            content = CVUtils.parseDoc(cvFile, env.getActiveProfiles());
            type = DocumentType.DOC;
        } else {
            content = CVUtils.parseDocX(cvFile, env.getActiveProfiles());
            type = DocumentType.DOCX;
        }
        CVModel cv = new CVModel(cvFile.getName(), type, Constant.API_URL + "/static/" + cvFile.getName(), content, new Date().getTime() + "");
        return cvRepository.save(cv);
    }
}
