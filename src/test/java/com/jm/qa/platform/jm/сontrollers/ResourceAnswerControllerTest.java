package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String url = "api/user/question/{questionId}/answer/{answerId}";

    @Test
    @WithMockUser
    @DataSet(value = {"roles.yml",
                      "users.yml",
                      "ResourceAnswerController/tag.yml",
                      "ResourceAnswerController/question.yml",
                      "ResourceAnswerController/answer.yml"},
            cleanBefore = true)
    void deleteAnswerById() throws Exception {
        int correctId = 100;
        int incorrectId = -100;
        mockMvc.perform(delete(url + correctId, 100).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
//        mockMvc.perform(delete(url + incorrectId, -100).
//                contentType(MediaType.APPLICATION_JSON)).
//                andExpect(jsonPath("$.id", -100)).
//                andExpect()
    }
}