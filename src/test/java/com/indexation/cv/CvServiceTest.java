package com.indexation.cv;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.service.CVService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest(classes = CvApplication.class)
public class CvServiceTest {
    @Autowired
    private CVService cvService;
    private CVModel cv;
    protected String cvId;
    private void setCvId(String id) { cvId=id; }

    @BeforeEach
    public void before() { cv = new CVModel("my_cv.pdf", DocumentType.PDF, "here", "This is a dummy cv", new Date().getTime()+""); }

    @AfterEach
    public void after() {
        CVModel testCV = cvService.searchById(cvId);
        cvService.delete(testCV);
    }

    @Test
    @DisplayName("Test if the file is well indexed and saved on the server")
    public void testSave() {
        CVModel testCV = cvService.save(cv);
        assertNotNull(testCV.getId());
        assertEquals(testCV.getType(), cv.getType());
        assertEquals(testCV.getUploadedDate(), cv.getUploadedDate());
        setCvId(testCV.getId());
    }

    @Test
    @DisplayName("Test if the file saved id is find by the api")
    public void testFindId() {
        CVModel newCv = cvService.save(cv);
        CVResponse testCV = cvService.searchById(newCv.getId());

        assertNotNull(testCV.getId());
        assertEquals(testCV.getType(), DocumentType.PDF.toString());
        assertEquals(testCV.getFilename(), "my_cv.pdf");
        assertEquals(testCV.getUrl(), "here");
        assertEquals(testCV.getContent(), "This is a dummy cv");
        setCvId(newCv.getId());
    }

    @Test
    @DisplayName("Test if the api find cvs with specific keywords")
    public void testFindByKeywords() {
        CVModel newCv = cvService.save(cv);
        System.out.println(newCv.getId());
        List<CVModel> testCV = cvService.searchCV("dummy");
        assertTrue(testCV.size() >= 1);
        List<CVModel> findCv = testCV.stream().filter(val -> Objects.equals(val.getId(), newCv.getId())).collect(Collectors.toList());
        assertEquals(findCv.size(), 1);
        setCvId(newCv.getId());
    }
}
