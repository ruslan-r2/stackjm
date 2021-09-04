package com.jm.qa.platform.jm.сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    String URL = "/api/user/question/{questionId}/answer";

    @Test
    @WithMockUser(roles = "ADMIN")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        AuthenticationRequestDTO correct = new AuthenticationRequestDTO("admin@admin.com","admin");
        String json = objectMapper.writeValueAsString(correct);
        this.mockMvc.perform(post(URL,99).contentType(MediaType.APPLICATION_JSON).content(json))
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
