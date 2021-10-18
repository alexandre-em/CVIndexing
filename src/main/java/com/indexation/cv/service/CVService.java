package com.indexation.cv.service;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import com.indexation.cv.repository.CVRepository;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Service
public class CVService {
    @Autowired
    private CVRepository cvRepository;

    public CVResponse searchById(String id) { return cvRepository.searchById(id); }
    public List<CVModel> searchCV(String keyword) { return cvRepository.search(keyword); }
    public CVModel saveCV(CVModel cv) {
        return cvRepository.save(cv);
    }

    public String parsePdf(MultipartFile file, String path) throws IOException {
        File tmp = new File(path);
        tmp.createNewFile();
        OutputStream os = new FileOutputStream(tmp);
        os.write(file.getBytes());
        // Parsing the pdf
        PDFParser parser = new PDFParser(new RandomAccessFile(tmp, "r"));
        parser.parse();
        COSDocument cos = parser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cos);
        String content = pdfTextStripper.getText(pdDoc);
        cos.close();
        pdDoc.close();
        return content;
    }
}
