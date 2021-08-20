package com.javamentor.qa.platform.webapp.controllers.rest;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebMvcTest(ResourceAnswerController.class)
class ResourceAnswerControllerTest {

    @Autowired
    MockMvc mockMvc;
    String URL = "api/user/question/{questionId}/answer/{answerId}";

    @Test
    void deleteAnswerById(Long questionId, Long answerId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL).
                param("questionId", questionId.toString()).
                param("answerId", answerId.toString()).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }
}