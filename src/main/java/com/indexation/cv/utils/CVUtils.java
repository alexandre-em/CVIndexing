package com.indexation.cv.utils;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains methods used by CVService
 */
public class CVUtils {
    public static File convertMultipartFile(MultipartFile file, String[] env) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String filename = timeStamp+"_"+file.getOriginalFilename();
        String newPath = "./static/"+filename;
        CVLogger.info("Converting MultipartFile to File", env);
        File tmp = new File(newPath);
        tmp.createNewFile();
        OutputStream os = new FileOutputStream(tmp);
        os.write(file.getBytes());
        CVLogger.info("End converting MultipartFile to File", env);
        return tmp;
    }

    public static String parsePdf(File file, String[] env) throws IOException {
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        CVLogger.info("Parsing file to string with PdfParser", env);
        parser.parse();
        CVLogger.info("End Parsing file to string with PdfParser", env);
        COSDocument cos = parser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cos);
        String content = pdfTextStripper.getText(pdDoc);
        CVLogger.info("Closing parser", env);
        cos.close();
        pdDoc.close();
        return content;
    }

    public static String parseDoc(File file, String[] env) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        HWPFDocument doc = new HWPFDocument(fis);
        CVLogger.info("Parsing file to string with WordExtractor", env);
        WordExtractor extractor = new WordExtractor(doc);
        CVLogger.info("End Parsing file to string with WordExtractor", env);
        return extractor.getText();
    }

    public static String parseDocX(File file, String[] env) throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
        CVLogger.info("Parsing file to string with XWPFWordExtractor", env);
        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
        CVLogger.info("End Parsing file to string with XWPFWordExtractor", env);
        return extractor.getText();
    }
}
