package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    @DataSet(value = "resource_answer_controller/voteAnswer.yml", cleanBefore = true, cleanAfter = true)
    public void voteAnswer() throws Exception{
        long questionIdCorrect = 100L;
        long answerIdCorrect = 100L;
        long answerIdIncorrect = -100L;
        username = "user@mail.ru";
        password = "user";
        //ответ и репутация автора ответа до повышения оценки
        Answer answerBefore = (Answer) entityManager.createQuery("select a from Answer a where a.id = :ansId")
                .setParameter("ansId",answerIdCorrect).getSingleResult();
        Optional<Reputation> reputationBefore = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("select r from Reputation r where r.author.id =:id")
                        .setParameter("id",answerBefore.getUser().getId()));
        Optional<VoteAnswer> voteBefore = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId")
                .setParameter("ansId",100L));
        assertFalse(voteBefore.isPresent()); //проверка что оценки на ответ нет
        //повышение оценки ответа
        mockMvc.perform(post(URL+"/"+answerIdCorrect+"/upVote",questionIdCorrect).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        //ответ и репутация автора ответа после повышения оценки
        Answer answerAfter = (Answer) entityManager.createQuery("select a from Answer a where a.id = :ansId")
                .setParameter("ansId",answerIdCorrect).getSingleResult();
        Optional<Reputation> reputationAfter = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("select r from Reputation r where r.author.id =:id")
                .setParameter("id",answerAfter.getUser().getId()));
        VoteAnswer voteAnswerList = (VoteAnswer) entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId and v.user.id = :senderId")
                        .setParameter("ansId",answerIdCorrect).setParameter("senderId",100L).getSingleResult();
        assertTrue(answerBefore.getId() == answerAfter.getId()); //проверка что ответ тот же
        assertTrue(voteAnswerList.getVote() == 1); //проверка что оценка положительная
        assertFalse(reputationBefore.isPresent());//проверка что до повышения репутации у автора нет
        assertTrue(reputationAfter.get().getCount() == 10); //проверка что репутация у автора ответа увеличилась на 10
        //изменение своей оценки на минус
        mockMvc.perform(post(URL+"/"+answerIdCorrect+"/downVote",questionIdCorrect).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        //ответ и репутация автора после изменения  оценки
        Answer answerAfterChange = (Answer) entityManager.createQuery("select a from Answer a where a.id = :ansId")
                .setParameter("ansId",answerIdCorrect).getSingleResult();
        Optional<Reputation> reputationAfterChange = SingleResultUtil.getSingleResultOrNull(entityManager.createQuery("select r from Reputation r where r.author.id =:id")
                .setParameter("id",answerAfterChange.getUser().getId()));
        VoteAnswer voteAnswerListChange =(VoteAnswer) entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId and v.user.id = :senderId")
                .setParameter("ansId",answerIdCorrect).setParameter("senderId",100L).getSingleResult();
        assertTrue(answerAfter.getId() == answerAfterChange.getId()); //проверка что ответ тот же
        assertTrue(voteAnswerListChange.getVote() == -1); //проверка что оценка отрицательная
        assertTrue(reputationAfterChange.get().getCount() == -5); //проверка что репутация у автора ответа уменьшилаь на 5

        //логин автора ответа
        username = "user2@mail.ru";
        password = "user";
        //Попытка повышения оценки своего ответа
        mockMvc.perform(post(URL+"/"+answerIdCorrect+"/upVote",questionIdCorrect).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //повышение оценки не существующего ответа
        mockMvc.perform(post(URL+"/"+answerIdIncorrect+"/upVote",questionIdCorrect).header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}


