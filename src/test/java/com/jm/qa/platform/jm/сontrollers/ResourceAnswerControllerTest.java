package com.jm.qa.platform.jm.сontrollers;

import com.javamentor.qa.platform.webapp.controllers.rest.ResourceAnswerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/config/application-test.properties")
public class ResourceAnswerControllerTest {

    @Autowired
    private ResourceAnswerController resourceAnswerController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addAnswerToQuestionTest() throws Exception {

    }

}
