package com.jm.qa.platform.jm.сontrollers;

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

    String URL = "api/user/question/{questionId}/answer/{answerId}";

    @Test
    void deleteAnswerById() throws Exception {

    }

    @AfterEach
    private void resetDb() {

    }
}