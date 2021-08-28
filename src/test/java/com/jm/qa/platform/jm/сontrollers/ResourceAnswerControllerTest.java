package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.javamentor.qa.platform.webapp.controllers.rest.ResourceAnswerController;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private ResourceAnswerController resourceAnswerController;
    private String url = "api/user/question/{questionId}/answer";

    @Test
    public void controllerInitializedCorrectly() {
        assertThat(resourceAnswerController).isNotNull();
    }

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