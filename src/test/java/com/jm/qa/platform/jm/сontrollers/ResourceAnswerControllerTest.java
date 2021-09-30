package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";
    private String markAnswerToDeleteUrl = "/api/user/question/{questionId}/answer/{answerId}";
    private String addCommentToAnswerUrl = "/api/user/question/{questionId}/answer/{answerId}/comment";
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
    @DisplayName("Return 400 bad request")
    @DataSet(value = "resource_answer_controller/getAllAnswers.yml", cleanBefore = true, cleanAfter = true)
    public void addAnswerToQuestionTest_isBadRequest() throws Exception {
        String jsonAnswerDto = objectMapper.writeValueAsString(new AnswerDto());
        this.mockMvc.perform(post(URL, 99).header("Authorization", getToken("user@mail.ru", "user"))
                .contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Return 200 question id exists")
    @DataSet(value = "resource_answer_controller/getAllAnswers.yml", cleanBefore = true, cleanAfter = true)
    public void addAnswerToQuestionTest_getQuestionId() throws Exception {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setUserId(101L);
        answerDto.setQuestionId(100L);
        answerDto.setBody("body");

        String jsonAnswerDto = objectMapper.writeValueAsString(answerDto);

        Optional<Answer> answerBefore = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select a from Answer a where a.id = 1"));
        assertFalse(answerBefore.isPresent());

        mockMvc.perform(post(URL, 100).header("Authorization", getToken("user@mail.ru", "user"))
                .contentType(MediaType.APPLICATION_JSON).content(jsonAnswerDto))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(101)))
                .andExpect(jsonPath("$.questionId", is(100)))
                .andExpect(jsonPath("$.countUserReputation", is(2)));

        Optional<Answer> answerAfter = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select a from Answer a where a.id = 1"));
        assertTrue(answerAfter.isPresent());
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
        List<VoteAnswer> voteBefore = entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId").setParameter("ansId",answerIdCorrect).getResultList();
        assertTrue(voteBefore.isEmpty());//проверка что список оценок ответа пустой
        assertFalse(reputationBefore.isPresent());//проверка что до повышения репутации у автора ответа нет
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
        List<VoteAnswer> voteAfter = entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId").setParameter("ansId",answerIdCorrect).getResultList();

        assertTrue(answerBefore.getId() == answerAfter.getId()); //проверка что ответ тот же
        assertTrue(voteAfter.size() == 1);//проверка что 1 оценка
        assertTrue(voteAfter.get(0).getVote() == 1); //проверка что оценка положительная
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
        List<VoteAnswer> voteAfterChange = entityManager.createQuery("select v from VoteAnswer v where v.answer.id = :ansId").setParameter("ansId",answerIdCorrect).getResultList();

        assertTrue(answerAfterChange.getId() == answerAfterChange.getId()); //проверка что ответ тот же
        assertTrue(voteAfterChange.size() == 1);//проверка что оценка также одна
        assertTrue(voteAfterChange.get(0).getVote() == -1); //проверка что оценка отрицательная
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

    @Test
    @DisplayName("Return 404 answer no found")
    @DataSet(value = "resource_answer_controller/addCommentToAnswer.yml",
            cleanBefore = true, cleanAfter = true)
    public void addCommentToAnswer_isNoFound() throws Exception {
        username = "user@mail.ru";
        password = "user";
        String comment = "test comment";
        String jsonCommentAnswerDto = objectMapper.writeValueAsString(comment);
        mockMvc.perform(post(addCommentToAnswerUrl, 99, 99)
                .header("Authorization", getToken(username, password))
                .contentType(MediaType.APPLICATION_JSON).content(jsonCommentAnswerDto))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Return 200 answer id exists")
    @DataSet(value = "resource_answer_controller/addCommentToAnswer.yml", cleanBefore = true, cleanAfter = true)
    public void addCommentToAnswer_isOk() throws Exception {
        username = "user@mail.ru";
        password = "user";
        String jsonCommentAnswerDto = objectMapper.writeValueAsString("test comment");

        Optional<CommentAnswer> commentBefore = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select ca from CommentAnswer ca where ca.id = 1"));
        assertFalse(commentBefore.isPresent());

        mockMvc.perform(post(addCommentToAnswerUrl, 100, 100)
                .header("Authorization", getToken(username, password))
                .contentType(MediaType.APPLICATION_JSON).content(jsonCommentAnswerDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(101)))
                .andExpect(jsonPath("$.answerId", is(100)))
                .andExpect(jsonPath("$.reputation", is(2)));

        Optional<CommentAnswer> commentAfter = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select ca from Comment ca where ca.id = 1"));
        assertTrue(commentAfter.isPresent());
    }
}


