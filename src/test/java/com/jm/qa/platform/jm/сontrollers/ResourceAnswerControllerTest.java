package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.UserTransaction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResourceAnswerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager entityManager;

    private String deleteAnswerUrl = "/api/user/question/{questionId}/answer/{answerId}";
    private String authenticationUrl = "/api/auth/token";

    @Test
    @WithMockUser(username = "admin@mail.ru",
                  password = "admin",
                  roles = "ADMIN")
    @DataSet(value = {"ResourceAnswerController/roles.yml",
                      "ResourceAnswerController/users.yml",
                      "ResourceAnswerController/tag.yml",
                      "ResourceAnswerController/question.yml",
                      "ResourceAnswerController/answer.yml"},
            cleanBefore = true)
    void deleteAnswerById() throws Exception {

//        Проверяется состояние флага IsDeleted у ответа с id 100, до его пометки на удаление
        Answer answerBeforeDelete = (Answer) entityManager.createQuery("from Answer answer where answer.id = 100").getSingleResult();
        assertFalse(answerBeforeDelete.getIsDeleted());

//        Выполняется запрос с пометкой на удаление ответа с id 100(корректный id)
        mockMvc.perform(delete(deleteAnswerUrl, 100, 100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isOk());

//        Проверяется состояние флага IsDeleted у ответа с id 100, после его пометки на удаление
        Answer answerAfterDelete = (Answer) entityManager.createQuery("from Answer answer where answer.id = 100").getSingleResult();
        assertTrue(answerAfterDelete.getIsDeleted());

//        Выполняется запрос с пометкой на удаление ответа с id -100(некорректный шв)
        mockMvc.perform(delete(deleteAnswerUrl, 100, -100)).
                andDo(print()).
                andExpect(authenticated()).
                andExpect(status().isBadRequest());
    }
}