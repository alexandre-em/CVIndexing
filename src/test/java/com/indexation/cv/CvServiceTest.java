package com.indexation.cv;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.CVResponse;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.service.CVService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CvApplication.class)
public class CvServiceTest {
    @Autowired
    private CVService cvService;
    protected String cvId;
    private void setCvId(String id) { cvId=id; }
    private CVModel cv = new CVModel("my_cv.pdf", DocumentType.PDF, "here", "This is a dummy cv", new Date().getTime()+"");

    @After
    public void after() {
        CVModel testCV = cvService.searchById(cvId);
        cvService.delete(testCV);
    }

    @Test
    public void testSave() {
        CVModel testCV = cvService.saveCV(cv);
        assertNotNull(testCV.getId());
        assertEquals(testCV.getType(), cv.getType());
        assertEquals(testCV.getUploadedDate(), cv.getUploadedDate());
        setCvId(testCV.getId());
    }

    @Test
    public void testFindId() {
        CVModel newCv = cvService.saveCV(cv);
        CVResponse testCV = cvService.searchById(newCv.getId());

        assertNotNull(testCV.getId());
        assertEquals(testCV.getType(), DocumentType.PDF.toString());
        assertEquals(testCV.getFilename(), "my_cv.pdf");
        assertEquals(testCV.getUrl(), "here");
        assertEquals(testCV.getContent(), "This is a dummy cv");
        setCvId(newCv.getId());
    }

    @Test
    public void testFindByKeywords() {
        CVModel newCv = cvService.saveCV(cv);
        System.out.println(newCv.getId());
        List<CVModel> testCV = cvService.searchCV("dummy");
        assertTrue(testCV.size() >= 1);
        List<CVModel> findCv = testCV.stream().filter(val -> Objects.equals(val.getId(), newCv.getId())).collect(Collectors.toList());
        assertEquals(findCv.size(), 1);
        setCvId(newCv.getId());
    }
}
