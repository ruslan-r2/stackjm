package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    String URL = "/api/user/question/{questionId}/answer";

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser
    @Test
    @DataSet(value = {"userResourceController/users.yml",
            "userResourceController/roles.yml",
            "userResourceController/reputations.yml",
            "userResourceController/answers.yml",
            "userResourceController/questions.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        mockMvc.perform(post(URL, 101L))
                .andExpect(status().isNotFound());
    }
}
