package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.dao.impl.model.AnswerDaoImpl;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    private AnswerDaoImpl answerDao;

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

        Answer answerBeforeDelete = answerDao.getById(100L).get();
        assertFalse(answerBeforeDelete.getIsDeleted());
        answerDao.deleteById(100L);
        Answer answerAfterDelete = answerDao.getById(100L).get();
        assertTrue(answerAfterDelete.getIsDeleted());

        mockMvc.perform(delete(url, 100, 100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isOk());

        mockMvc.perform(delete(url, 100, -100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isBadRequest());
    }
}