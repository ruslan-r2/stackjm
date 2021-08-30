package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    String URL = "/api/user/question/{questionId}/answer";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @Test
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml",
            "resourceAnswerController/reputations.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        mockMvc.perform(post(URL, 101L))
                .andExpect(status().isNotFound());
    }
}
