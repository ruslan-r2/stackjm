package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @PersistenceContext
    EntityManager entityManager;

    private String username;
    private String password;

    private String deleteAnswerByIdApiUrl = "/api/user/question/{questionId}/answer/{answerId}";

    @Test
    @DataSet(value = {"ResourceAnswerController/answer.yml",
                      "ResourceAnswerController/users.yml",
                      "ResourceAnswerController/question.yml",
                      "ResourceAnswerController/roles.yml",
                      "ResourceAnswerController/tag.yml"})
    public void deleteAnswerById() throws Exception {

        username = "admin@mail.ru";
        password = "admin";

        Answer answerBeforeDelete = (Answer) entityManager.createQuery("select a from Answer a where a.id = 100").getSingleResult();
        assertFalse(answerBeforeDelete.getIsDeleted());

        mockMvc.perform(post(deleteAnswerByIdApiUrl, 100, 100).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isOk());

        Answer answerAfterDelete = (Answer) entityManager.createQuery("select a from Answer a where a.id = 100").getSingleResult();
        assertTrue(answerAfterDelete.getIsDeleted());

        mockMvc.perform(post(deleteAnswerByIdApiUrl, 100, -100).
                header("Authorization", getToken(username, password))).
                andDo(print()).
                andExpect(status().isBadRequest());
    }
}


