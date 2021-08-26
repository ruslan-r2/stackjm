package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest() throws Exception {
        

    }

}
