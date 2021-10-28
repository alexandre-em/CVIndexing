package com.indexation.cv;

import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.service.CVService;
import com.indexation.cv.utils.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.*;

/**
 * Test all web endpoints and its methods of this API with different scenarios
 */
@SpringBootTest(classes = CvApplication.class)
public class CVWebIntegrationTest {
    @Autowired
    private CVService cvService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @DisplayName("Test if the GET `/api/v1/cv` find and return cv matching the keywords")
    public void testFindCVByKeywords() {
        ResponseEntity<String> response = restTemplate.getForEntity(Constant.API_URL+"/api/v1/cv?keyword=java", String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DisplayName("Test if the GET `/api/v1/cv/{id}` find and return the cv details")
    public void testFindCVById() {
        CVModel cvModel = new CVModel("test-toto", DocumentType.PDF, "path_to_toto_cv","Je m'appelle Toto et je fais du java", new Date().getTime()+"");
        cvModel = cvService.save(cvModel);

        ResponseEntity<String> response = restTemplate.getForEntity(Constant.API_URL+"/api/v1/cv/"+cvModel.getId(), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        cvService.delete(cvModel);
    }

    @Test
    @DisplayName("Test if the GET `/api/v1/cv/{id}` doesn't find a random cv id ")
    public void failTestFindCVById() {
        String randomId = "mon-id-il-est-trop-bien";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(Constant.API_URL+"/api/v1/cv/"+randomId, String.class);
            assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND); // Must throw an exception so not pass this line
        } catch (Exception e) {
            assertEquals(parseInt(e.getLocalizedMessage().split(" ")[0]), HttpStatus.NOT_FOUND.value());
        }
    }

    @Test
    @DisplayName("Test if the POST `/api/v1/cv` doesn't accept a file with an extension that is not a pdf/word file")
    public void failTestIndexFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> parametersReq2 = new LinkedMultiValueMap<>();
        parametersReq2.add("file", new FileSystemResource("run.sh"));
        HttpEntity<MultiValueMap<String, Object>> requestEntityReq2 = new HttpEntity<>(parametersReq2, headers);
        try {
            ResponseEntity<String> response2 = restTemplate.exchange(Constant.API_URL + "/api/v1/cv", HttpMethod.POST, requestEntityReq2, String.class);
            assertEquals(response2.getStatusCode(), HttpStatus.UNSUPPORTED_MEDIA_TYPE); // Must throw an exception so not pass this line
        } catch (Exception e) {
            assertEquals(parseInt(e.getLocalizedMessage().split(" ")[0]), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        }
    }

    private void cVPostCall(String filePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new FileSystemResource(filePath));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = restTemplate.exchange(Constant.API_URL+"/api/v1/cv", HttpMethod.POST,requestEntity,String.class);
        System.out.println(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Test if the POST `/api/v1/cv` upload and index the pdf file into elasticsearch")
    public void testIndexPDFFile() {
        cVPostCall("input/cv.pdf");
    }

    @Test
    @DisplayName("Test if the POST `/api/v1/cv` upload and index the doc file into elasticsearch")
    public void testIndexDocFile() {
        cVPostCall("input/Rouquier_CV.doc");
    }

    @Test
    @DisplayName("Test if the POST `/api/v1/cv` upload and index the docx file into elasticsearch")
    public void testIndexDocxFile() {
        cVPostCall("input/dijou_CV.docx");
    }
}
