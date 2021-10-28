package com.indexation.cv;

import com.indexation.cv.utils.CVUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CvUtilTest {
    @Test
    @DisplayName("Test if the pdf file is well parsed")
    public void testPDFParsing() throws IOException {
        File fileTest = new File("input/cv.pdf");
        String pdfContent = CVUtils.parsePdf(fileTest);
        assertTrue(pdfContent.contains("github.com/marvinroger"));
        assertTrue(pdfContent.contains("React"));
        assertTrue(pdfContent.contains("TOEIC"));

        fileTest = new File("input/164316.pdf");
        pdfContent = CVUtils.parsePdf(fileTest);
        assertTrue(pdfContent.contains("Fersi Karim"));
        assertTrue(pdfContent.contains("Spring boot"));
    }

    @Test
    @DisplayName("Test if the doc file is well parsed")
    public void testDOCParsing() throws IOException, InvalidFormatException {
        File fileTest = new File("input/Rouquier_CV.doc");
        String docContent = CVUtils.parseDoc(fileTest);
        assertTrue(docContent.contains("Jean-Baptiste Rouquier"));
        assertTrue(docContent.contains("Java"));

        fileTest = new File("input/dijou_CV.doc");
        docContent = CVUtils.parseDoc(fileTest);
        assertTrue(docContent.contains("PAUL DIJOU"));
        assertTrue(docContent.contains("Java"));
    }

    @Test
    @DisplayName("Test if the docx file is well parsed")
    public void testDOCXParsing() throws IOException, InvalidFormatException {
        File fileTest = new File("input/dijou_CV.docx");
        String docxContent = CVUtils.parseDocX(fileTest);
        assertTrue(docxContent.contains("PAUL DIJOU"));
        assertTrue(docxContent.contains("Java"));
    }
}
