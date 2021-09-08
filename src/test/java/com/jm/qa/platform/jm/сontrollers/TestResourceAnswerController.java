package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestResourceAnswerController extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";
    private String JWTurl = "/api/auth/token";
    private String token;
    @Autowired
    private MockMvc mockMvc;

    @Test
//    @WithMockUser(username = "user@mail.ru", roles = "USER")
    @DataSet(value = "userResourceController/getAllAnswers.yml", cleanBefore = true
            , cleanAfter = true
    )

    public void getAllAnswers() throws Exception {
        int idWithAnswers = 100;
        int idWithoutAnswers = 101;
        int idIncorrect = -100;

        AuthenticationRequestDTO correct = new AuthenticationRequestDTO("admin@mail.ru","admin");
        String json = objectMapper.writeValueAsString(correct);
        this.mockMvc.perform(post(JWTurl).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
        .andExpect(content().encoding(token));

        //Существующий ид вопроса с ответами,ожидание массива из 2-х ответов
        this.mockMvc.perform(get(URL, idWithAnswers).header("Authorization",token))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2))) // величина ожидаемого массива
                //1 ответ
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].userId", is(101)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].nickname", is("user")))
                .andExpect(jsonPath("$[0].body", is("text")))
                .andExpect(jsonPath("$[0].countValuable", is(2)))
                .andExpect(jsonPath("$[0].countUserReputation", is(2)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                // 2 ответ
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].userId", is(102)))
                .andExpect(jsonPath("$[1].questionId", is(100)))
                .andExpect(jsonPath("$[1].nickname", is("user2")))
                .andExpect(jsonPath("$[1].body", is("text2")))
                .andExpect(jsonPath("$[1].countValuable", is(-1)))
                .andExpect(jsonPath("$[1].countUserReputation", is(0)))
                .andExpect(jsonPath("$[1].questionId", is(100)));
        // Существующий ид вопроса без ответов, ожидание пустого массива
        this.mockMvc.perform(get(URL, idWithoutAnswers))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        //Не существующий ид вопроса
        this.mockMvc.perform(get(URL, idIncorrect))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isNotFound());
    }
}


