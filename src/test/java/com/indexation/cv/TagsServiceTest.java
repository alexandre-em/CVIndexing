package com.indexation.cv;

import com.indexation.cv.Exception.TagException;
import com.indexation.cv.data.CVModel;
import com.indexation.cv.data.DocumentType;
import com.indexation.cv.data.Tags;
import com.indexation.cv.service.CVService;
import com.indexation.cv.service.TagsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CvApplication.class)
public class TagsServiceTest {
    @Autowired
    private TagsService tagsService;
    @Autowired
    private CVService cvService;
    private CVModel cv;
    private Tags tags;

    @BeforeEach
    public void before() throws TagException {
        cv = cvService.save(new CVModel("my_cv.pdf", DocumentType.PDF, "here", "This is a dummy cv", new Date().getTime()+""));
        String keywords = "java,node,python,test_skill";
        tags = tagsService.createTags(cv.getId(), Arrays.asList(keywords.split(",")));
    }
    @AfterEach
    public void after() {
        cvService.delete(cv);
        tagsService.removeTags(tags);
    }

    @Test
    @DisplayName("Test find a `Tags` by its `id`")
    public void getTagsByIdTest() throws TagException {
        Tags testTags = tagsService.getTagsById(tags.getId());
        assertEquals(tags.getTags(), testTags.getTags());
        assertEquals(tags.getCvId(), testTags.getCvId());
    }

    @Test
    @DisplayName("Test find a `Tags` by its linked cv `id`")
    public void getTagsByCvIdTest() throws TagException {
        Tags testTags = tagsService.getTagsByCvId(cv.getId());
        assertEquals(tags.getTags(), testTags.getTags());
        assertEquals(tags.getId(), testTags.getId());
    }

    @Test
    @DisplayName("Test find Tags by a single or multiple matching keyword")
    public void getTagsByKeywords() throws TagException {
        // Case where all keywords must match
        List<Tags> testTags = tagsService.getTagsByKeywords("test_skill,java", true);
        assertEquals((int) testTags.stream().filter(tag -> tag.getId().equals(tags.getId())).count(), 1);
        // Case where at least one keyword must match
        testTags = tagsService.getTagsByKeywords("test_skill,java", false);
        assertEquals((int) testTags.stream().filter(tag -> tag.getId().equals(tags.getId())).count(), 1);
    }

    @Test
    @DisplayName("Test if tags have successfully been added")
    public void updateTagsByCvIdTest() throws TagException {
        String newTags = "jupyter,vuejs";
        Tags testTags = tagsService.updateTagsByCvId(cv.getId(), Arrays.asList(newTags.split(",")));
        Tags testTags2 = tagsService.getTagsById(testTags.getId());
        assertEquals(testTags.getTags(), testTags2.getTags());
        assertTrue(testTags2.getTags().contains("jupyter"));
        assertTrue(testTags2.getTags().contains("vuejs"));
    }
}
