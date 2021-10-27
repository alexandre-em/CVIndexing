package com.indexation.cv.service;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import com.indexation.cv.repository.CVRepository;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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
    public CVModel saveCV(CVModel cv) { return cvRepository.save(cv); }
    public void delete(CVModel cv) { cvRepository.delete(cv); }

    public static File convertMultipartFile(MultipartFile file, String path) throws IOException {
        CVLogger.info("Converting MultipartFile to File");
        File tmp = new File(path);
        tmp.createNewFile();
        OutputStream os = new FileOutputStream(tmp);
        os.write(file.getBytes());
        CVLogger.info("End converting MultipartFile to File");
        return tmp;
    }
    public String parsePdf(File file) throws IOException {
        // Parsing the pdf file
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        CVLogger.info("Parsing file to string with PdfParser");
        parser.parse();
        CVLogger.info("End Parsing file to string with PdfParser");
        COSDocument cos = parser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cos);
        String content = pdfTextStripper.getText(pdDoc);
        CVLogger.info("Closing parser");
        cos.close();
        pdDoc.close();
        return content;
    }
    public String parseDoc(File file) throws IOException, InvalidFormatException {
        // Parsing the doc file
        FileInputStream fis = new FileInputStream(file);
        HWPFDocument doc = new HWPFDocument(fis);
        CVLogger.info("Parsing file to string with WordExtractor");
        WordExtractor extractor = new WordExtractor(doc);
        CVLogger.info("End Parsing file to string with WordExtractor");
        return extractor.getText();
    }
    public String parseDocX(File file) throws IOException, InvalidFormatException {
        // Parsing the docx file
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
        CVLogger.info("Parsing file to string with XWPFWordExtractor");
        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
        CVLogger.info("End Parsing file to string with XWPFWordExtractor");
        return extractor.getText();
    }
}
