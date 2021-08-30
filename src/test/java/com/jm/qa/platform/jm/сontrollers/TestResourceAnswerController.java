package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestResourceAnswerController extends AbstractIntegrationTest {

    private String URL = "http://localhost:8080/api/user/question/{questionId}/answer";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet( value = {"userResourceController/roles.yml", "userResourceController/users.yml", "userResourceController/tags.yml",
            "userResourceController/questions.yml", "userResourceController/answers.yml"},cleanBefore = true)
    @WithUserDetails("admin@mail.ru")
    public void getAllAnswersCorrect() throws Exception{
        int idCorrect = 100;
        this.mockMvc.perform(get(URL,idCorrect))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",is(100)))
                .andExpect(jsonPath("$[0].userId",is(100)))
                .andExpect(jsonPath("$[0].nickname",is("admin")))
                .andExpect(jsonPath("$[0].body",is("text")));
    }
    @Test
    @DataSet( value = {"userResourceController/roles.yml", "userResourceController/users.yml", "userResourceController/tags.yml",
            "userResourceController/questions.yml", "userResourceController/answers.yml"},cleanBefore = true)
    @WithUserDetails("admin@mail.ru")
    public void getAllAnswersIncorrect() throws Exception{
        int idIncorrect = 1000000;
        this.mockMvc.perform(get(URL,idIncorrect))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(content().string("[]"));
    }

}


