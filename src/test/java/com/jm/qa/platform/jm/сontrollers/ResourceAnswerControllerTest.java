package com.jm.qa.platform.jm.сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    String URL = "/api/user/question/{questionId}/answer";

    @Test
    @DisplayName("Return 404 question id not found")
    @WithMockUser(roles = "ADMIN")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        String jsonAnswerDto = objectMapper.writeValueAsString(new AnswerDto());
        mockMvc.perform(post(URL,99).contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 200 question id exists")
    @WithMockUser(roles = "ADMIN")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"})
    public void addAnswerToQuestionTest_getQuestionId() throws Exception {
        AnswerDto answerdto = new AnswerDto();
        answerdto.setId(100L);
        String jsonAnswerDto = objectMapper.writeValueAsString(answerdto);
                mockMvc.perform(post(URL, 100).contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id"). value(100L));
    }
}
