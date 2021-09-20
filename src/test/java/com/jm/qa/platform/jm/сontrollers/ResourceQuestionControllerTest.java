package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ResourceQuestionControllerTest extends AbstractIntegrationTest {
    private String username;
    private String password;
    private String token;

    @PersistenceContext
    private EntityManager entityManager;

    private long getReputationByIdUser(Long idUser) {
        return entityManager.createQuery("SELECT SUM(r.count) FROM Reputation r WHERE r.author.id = :id", Long.class)
                .setParameter("id", idUser)
                .getSingleResult();
    }

    @Test
    @DataSet(value = "resource_question_controller/getById.yml", cleanBefore = true, cleanAfter = true)
    public void getById() throws Exception {
        int correctId = 100;
        int incorrectId = -100;
        username = "user@mail.ru";
        password = "user";
        String URL = "/api/user/question/{id}";

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

    @Test
    @DataSet(value = {"resource_question_controller/vote_question/questions.yml", "resource_question_controller/vote_question/roles.yml", "resource_question_controller/vote_question/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_status_not_found() throws Exception {
        username = "user101@mail.ru";
        password = "user101";

        mockMvc.perform(post("/api/user/question/99/upVote").header("Authorization", getToken(username, password)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"resource_question_controller/vote_question/questions.yml", "resource_question_controller/vote_question/roles.yml", "resource_question_controller/vote_question/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void testing_upVote_and_downVote() throws Exception {
        username = "user101@mail.ru";
        password = "user101";
        token = getToken(username, password);

        // положительный голос 101-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        Assert.assertEquals(getReputationByIdUser(100L), 10L);

        // повторный положительный голос 101-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        Assert.assertEquals(getReputationByIdUser(100L), 10L);

        // положительный голос 101-ого за другой вопрос
        mockMvc.perform(post("/api/user/question/101/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        Assert.assertEquals(getReputationByIdUser(100L), 20L);

        username = "user102@mail.ru";
        password = "user102";
        token = getToken(username, password);

        // положительный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(2)));

        Assert.assertEquals(getReputationByIdUser(100L), 30L);

        // отрицательный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/downVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));

        Assert.assertEquals(getReputationByIdUser(100L), 15L);

        // повторный отрицательный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/downVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));

        Assert.assertEquals(getReputationByIdUser(100L), 15L);
    }

    @Test
    @DataSet(value = {"resource_question_controller/vote_question/questions.yml", "resource_question_controller/vote_question/roles.yml", "resource_question_controller/vote_question/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void user_votes_himself_method_should_return_status_bad_request() throws Exception {
        username = "user100@mail.ru";
        password = "user100";

        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", getToken(username, password)))
                .andExpect(status().isBadRequest());
    }
}
