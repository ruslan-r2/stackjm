package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";

    @Test
    @DataSet(value = "userResourceController/getAllAnswers.yml", cleanBefore = true, cleanAfter = true)
    public void getAllAnswers() throws Exception {
        int idWithAnswers = 100;
        int idWithoutAnswers = 101;
        int idIncorrect = -100;
        String username = "user@mail.ru";
        String password = "user";

        //Существующий ID вопроса с ответами,ожидание массива из 2-х ответов
        mockMvc.perform(get(URL, idWithAnswers)
                .header("Authorization", getToken(username, password))
        )
                .andDo(print())
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
        // Существующий ID вопроса без ответов, ожидание пустого массива
        this.mockMvc.perform(get(URL, idWithoutAnswers).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        //Не существующий ID вопроса
        this.mockMvc.perform(get(URL, idIncorrect).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Return 404 question id not found")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_isNotFoundQuestionId() throws Exception {
        String jsonAnswerDto = objectMapper.writeValueAsString(new AnswerDto());
        mockMvc.perform(post(URL, 99).contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 200 question id exists")
    @DataSet(value = {"resourceAnswerController/answers.yml",
            "resourceAnswerController/questions.yml",
            "resourceAnswerController/reputations.yml",
            "resourceAnswerController/roles.yml",
            "resourceAnswerController/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void addAnswerToQuestionTest_getQuestionId() throws Exception {
        AnswerDto answerdto = new AnswerDto();
        answerdto.setId(100L);

        String jsonAnswerDto = objectMapper.writeValueAsString(answerdto);
        mockMvc.perform(post(URL, 100).contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$[0].id", equalTo(100)));
    }
}


