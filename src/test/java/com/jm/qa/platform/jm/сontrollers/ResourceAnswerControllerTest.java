package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";
    private String markAnswerToDeleteUrl = "/api/user/question/{questionId}/answer/{answerId}";
    private String username;
    private String password;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DataSet(value = "resource_answer_controller/getAllAnswers.yml", cleanBefore = true, cleanAfter = true)
    public void getAllAnswers() throws Exception {
        int idWithAnswers = 100;
        int idWithoutAnswers = 101;
        int idIncorrect = -100;
        username = "user@mail.ru";
        password = "user";

        //Существующий ID вопроса с ответами,ожидание массива из 2-х ответов
        mockMvc.perform(get(URL, idWithAnswers).header("Authorization", getToken(username, password)))
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
    @DataSet(value = "resource_answer_controller/markAnswerToDelete.yml",
             cleanBefore = true, cleanAfter = true)
    public void markAnswerToDelete() throws Exception {
        username = "user@mail.ru";
        password = "user";

        Answer answerBeforeDelete = (Answer) entityManager.createQuery("select a from Answer a where a.id = 100").getSingleResult();
        assertFalse(answerBeforeDelete.getIsDeleted());

        mockMvc.perform(delete(markAnswerToDeleteUrl, 100, 100).
                        header("Authorization", getToken(username, password))).
                andExpect(status().isOk());

        Answer answerAfterDelete = (Answer) entityManager.createQuery("select a from Answer a where a.id = 100").getSingleResult();
        assertTrue(answerAfterDelete.getIsDeleted());

        mockMvc.perform(delete(markAnswerToDeleteUrl, 100, -100).
                        header("Authorization", getToken(username, password))).
                andExpect(status().isBadRequest());
    }
}


