package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager entityManager;

    private String url = "/api/user/question/{questionId}/answer/{answerId}";

    @Test
    @WithMockUser
    @DataSet(value = {"ResourceAnswerController/roles.yml",
                      "ResourceAnswerController/users.yml",
                      "ResourceAnswerController/tag.yml",
                      "ResourceAnswerController/question.yml",
                      "ResourceAnswerController/answer.yml"},
            cleanBefore = true)
    void deleteAnswerById() throws Exception {

        Answer answerBeforeDelete = (Answer) entityManager.createQuery("from Answer answer where answer.id = 100").getSingleResult();
        assertFalse(answerBeforeDelete.getIsDeleted());

        mockMvc.perform(delete(url, 100, 100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isOk());

        Answer answerAfterDelete = (Answer) entityManager.createQuery("from Answer answer where answer.id = 100").getSingleResult();
        assertTrue(answerAfterDelete.getIsDeleted());

        mockMvc.perform(delete(url, 100, -100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isBadRequest());
    }
}