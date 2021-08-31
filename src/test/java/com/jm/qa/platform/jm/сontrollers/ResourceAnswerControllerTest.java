package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.webapp.controllers.rest.ResourceAnswerController;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String url = "api/user/question/{questionId}/answer";

    @Test
    @DataSet(value = {"roles.yml", "users.yml", "tag.yml", "question.yml", "answer.yml"})
    void deleteAnswerById() throws Exception {
        String idToDelete = "/1";
        mockMvc.perform(delete(url + idToDelete, 1).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }
}