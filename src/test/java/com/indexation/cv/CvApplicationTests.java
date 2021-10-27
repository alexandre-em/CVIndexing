package com.indexation.cv;

import com.indexation.cv.controller.CVResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest(classes = CvApplication.class)
class CvApplicationTests {
	@Autowired
	private CVResource cvResource;

	@Test
	void contextLoads() {
		assertNotNull(cvResource);
	}
}
