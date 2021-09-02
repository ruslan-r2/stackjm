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
import javax.persistence.PersistenceContext;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @PersistenceContext
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
    public void deleteAnswerById() throws Exception {
        String correctId = "100";
        String incorrectId = "-100";

        String deleteHql = "from Answer answer set answer.isDeleted = true where answer.id = " + correctId;
        entityManager.createQuery(deleteHql).executeUpdate();

        String checkDeletedHql = "from Answer answer where answer.id = " + correctId;
        Answer answer = (Answer) entityManager.createQuery(checkDeletedHql).getSingleResult();

        if (!answer.getIsDeleted()) {

        }

        mockMvc.perform(delete(url, correctId, correctId)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isOk());

        mockMvc.perform(delete(url, correctId, incorrectId)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isBadRequest());
    }
}