package com.jm.qa.platform.jm.сontrollers;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.impl.TestDataInitService;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TestDataInitService testDataInitService;

    String URL = "api/user/question/{questionId}/answer/{answerId}";

    @Test
    void deleteAnswerById() throws Exception {
        testDataInitService.createEntity();
        Question question = questionService.getById(1L).get();
    }

    @AfterEach
    private void resetDb() {

    }
}