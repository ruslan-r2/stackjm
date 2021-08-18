package com.javamentor.qa.platform.webapp.controllers.rest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class ResourceAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ResourceAnswerController resourceAnswerController;

    @Test
    void getQuestionById() {
    }

    @Test
    void deleteAnswerById() {
    }
}