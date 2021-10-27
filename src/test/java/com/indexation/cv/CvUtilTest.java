package com.indexation.cv;

import com.indexation.cv.CvApplication;
import com.indexation.cv.service.CVService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CvApplication.class)
public class CvUtilTest {
    @Autowired
    CVService cvService;

    @Test
    public void testPDFParsing() throws IOException {
        File fileTest = new File("input/cv.pdf");
        String pdfContent = cvService.parsePdf(fileTest);
        assertTrue(pdfContent.contains("github.com/marvinroger"));
        assertTrue(pdfContent.contains("React"));
        assertTrue(pdfContent.contains("TOEIC"));
        fileTest = new File("input/164316.pdf");
        pdfContent = cvService.parsePdf(fileTest);
        assertTrue(pdfContent.contains("Fersi Karim"));
        assertTrue(pdfContent.contains("Spring boot"));
    }

    @Test
    public void testDOCParsing() throws IOException, InvalidFormatException {
        File fileTest = new File("input/Rouquier_CV.doc");
        String docContent = cvService.parseDoc(fileTest);
        assertTrue(docContent.contains("Jean-Baptiste Rouquier"));
        assertTrue(docContent.contains("Java"));
        fileTest = new File("input/dijou_CV.doc");
        docContent = cvService.parseDoc(fileTest);
        assertTrue(docContent.contains("PAUL DIJOU"));
        assertTrue(docContent.contains("Java"));
    }

    @Test
    public void testDOCXParsing() throws IOException, InvalidFormatException {
        File fileTest = new File("input/dijou_CV.docx");
        String docxContent = cvService.parseDocX(fileTest);
        assertTrue(docxContent.contains("PAUL DIJOU"));
        assertTrue(docxContent.contains("Java"));
    }
}
