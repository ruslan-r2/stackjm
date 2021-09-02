package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    String URL = "/api/user/question/{questionId}/answer";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
//    @WithUserDetails("admin@mail.ru")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        mockMvc.perform(post(URL, 101))


                .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_getQuestionId() throws Exception {
        mockMvc.perform(post(URL, 100))
                .andExpect(status().isOk());

    }
}
