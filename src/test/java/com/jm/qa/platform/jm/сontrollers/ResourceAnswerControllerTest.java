package com.jm.qa.platform.jm.сontrollers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";
    private String markAnswerToDeleteUrl = "/api/user/question/{questionId}/answer/{answerId}";
    private String addCommentToAnswerUrl = "/api/user/question/{questionId}/answer/{answerId}/comment";
    private String  updateAnswerBodyUrl = "/api/user/question/{questionId}/answer/{answerId}/body";
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
        assertTrue(voteAfter.get(0).getVoteType().equals(VoteType.UP)); //проверка что оценка положительная
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
        assertTrue(voteAfterChange.get(0).getVoteType().equals(VoteType.DOWN)); //проверка что оценка отрицательная
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
    @DisplayName("Return 400 answer no exists")
    @DataSet(value = "resource_answer_controller/addCommentToAnswer.yml",
            cleanBefore = true, cleanAfter = true)
    public void addCommentToAnswer_isBadRequest() throws Exception {
        username = "user@mail.ru";
        password = "user";
        String comment = "test comment";
        String jsonCommentAnswerDto = objectMapper.writeValueAsString(comment);

        mockMvc.perform(post(addCommentToAnswerUrl, 99, 99)
                .header("Authorization", getToken(username, password))
                .contentType(MediaType.APPLICATION_JSON).content(jsonCommentAnswerDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Return 200 answer id exists")
    @DataSet(value = "resource_answer_controller/addCommentToAnswer.yml", cleanBefore = true, cleanAfter = true)
    public void addCommentToAnswer_isOk() throws Exception {
        username = "user@mail.ru";
        password = "user";
        String comment = objectMapper.writeValueAsString("test comment");

        Optional<CommentAnswer> commentBefore = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select ca from CommentAnswer ca where ca.id = 1"));
        assertFalse(commentBefore.isPresent());

        mockMvc.perform(post(addCommentToAnswerUrl, 100, 100)
                .header("Authorization", getToken(username, password))
                .contentType(MediaType.APPLICATION_JSON).content(comment))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(101)))
                .andExpect(jsonPath("$.answerId", is(100)))
                .andExpect(jsonPath("$.reputation", is(2)));

        Optional<CommentAnswer> commentAfter = SingleResultUtil.getSingleResultOrNull(entityManager
                .createQuery("select ca from CommentAnswer ca where ca.id = 1"));
        assertTrue(commentAfter.isPresent());
    }


//    private String  updateAnswerBodyUrl = "/api/user/question/{questionId}/answer/{answerId}/body";
//

    @Test
    @DataSet(value = "resource_answer_controller/updateAnswerBody.yml", cleanBefore = true, cleanAfter = true)
    public void updateAnswerBodyTest () throws Exception {
        int questionId = 100;
        int answerIdCorrect = 100;
        int answerIdIsDeleted = 102; //isDeleted = true
        int answerIdIsNotExist = 110; //нет в базе

        username = "user@mail.ru";
        password = "user";
        String updatedBody = "ALL RIGHT!";

        //создание Dto и JSON для теста
        AnswerDto updateAnswerDto = new AnswerDto();
        updateAnswerDto.setId(100L);
        updateAnswerDto.setUserId(101L);
        updateAnswerDto.setQuestionId(100L);
        updateAnswerDto.setBody(updatedBody);

        ObjectMapper objectMapper = new ObjectMapper();
        String incomingJson = objectMapper.writeValueAsString(updateAnswerDto);

        //Получения значения body до обновления
        Answer answer = (Answer) entityManager.createQuery("SELECT a FROM Answer a WHERE a.id = 100").getSingleResult();
        String bodyValueBeforeUpdate = answer.getHtmlBody();

        //Тест, валидные данные
        mockMvc.perform(put(updateAnswerBodyUrl, questionId , answerIdCorrect)
                        .contentType(MediaType.APPLICATION_JSON).content(incomingJson)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body", is(updatedBody)));

        //Тест, проверка обновился ли body
        mockMvc.perform(put(updateAnswerBodyUrl, questionId , answerIdCorrect)
                        .contentType(MediaType.APPLICATION_JSON).content(incomingJson)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body", is(not(bodyValueBeforeUpdate))));

        updateAnswerDto.setBody("");
        String incomingJsonEmptyBody = objectMapper.writeValueAsString(updateAnswerDto);

        //Тест, пустая строка в anwerbody
        mockMvc.perform(put(updateAnswerBodyUrl, questionId , answerIdCorrect)
                        .contentType(MediaType.APPLICATION_JSON).content(incomingJsonEmptyBody)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        //Тест, обращение к isDeleted = truе коментарию
        mockMvc.perform(put(updateAnswerBodyUrl, questionId , answerIdIsDeleted)
                        .contentType(MediaType.APPLICATION_JSON).content(incomingJson)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        //Тест, обращение к несуществующему коментарию
        mockMvc.perform(put(updateAnswerBodyUrl, questionId , answerIdIsNotExist)
                        .contentType(MediaType.APPLICATION_JSON).content(incomingJson)
                        .header("Authorization", getToken(username, password)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}