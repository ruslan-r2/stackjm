package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceQuestionControllerTest extends AbstractIntegrationTest {
    private String URL = "/api/user/question/{id}";
    private String username;
    private String password;

    @Test
    @DataSet(value = "resource_question_controller/getById.yml", cleanBefore = true, cleanAfter = true)
    public void getById() throws Exception {
        int correctId = 100;
        int incorrectId = -100;
        username = "user@mail.ru";
        password = "user";

        //Существующий ID вопроса
        mockMvc.perform(get(URL, correctId).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.title", is("question")))
                .andExpect(jsonPath("$.authorId", is(100)))
                .andExpect(jsonPath("$.authorName", is("just user")))
                .andExpect(jsonPath("$.authorImage", is("user.image.com" )))
                .andExpect(jsonPath("$.description", is("description1" )))
                .andExpect(jsonPath("$.viewCount", is(2)))
                .andExpect(jsonPath("$.authorReputation", is(1)))
                .andExpect(jsonPath("$.countAnswer", is(1)))
                .andExpect(jsonPath("$.countValuable", is(2)))
                .andExpect(jsonPath("$.persistDateTime", is("1990-10-10T00:00:00" )))
                .andExpect(jsonPath("$.lastUpdateDateTime", is("1990-10-10T00:00:00")))
        ;

        //Не существующий ID вопроса
        this.mockMvc.perform(get(URL, incorrectId).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
