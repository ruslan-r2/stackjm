package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestResourceAnswerController extends AbstractIntegrationTest {

    private String URL = "/api/user/question/{questionId}/answer";
    @Autowired
    private MockMvc mockMvc;

    @Test
//    @WithMockUser(username = "user@mail.ru", roles = "ROLE_USER")
    @DataSet(value = "userResourceController/getAllAnswers.yml", cleanBefore = true
            , cleanAfter = true
    )
    @WithUserDetails("user@mail.ru")
    public void getAllAnswers() throws Exception {
        int idCorrect = 100;
        int idIncorrect = -100;

        //проверка на величину ожидаемого массива
        this.mockMvc.perform(get(URL, idCorrect))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)));
        // Корректный ид, массив из 2-х ответов
        this.mockMvc.perform(get(URL, idCorrect))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isOk())
                //1 ответ
                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].userId", is(101)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                .andExpect(jsonPath("$[0].nickname", is("user")))
                .andExpect(jsonPath("$[0].body", is("text")))
                .andExpect(jsonPath("$[0].countValuable", is(2)))
                .andExpect(jsonPath("$[0].questionId", is(100)))
                // 2 ответ
                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].userId", is(102)))
                .andExpect(jsonPath("$[1].questionId", is(100)))
                .andExpect(jsonPath("$[1].nickname", is("user2")))
                .andExpect(jsonPath("$[1].body", is("text2")))
                .andExpect(jsonPath("$[1].countValuable", is(-1)))
                .andExpect(jsonPath("$[1].questionId", is(100)));
        // Некорректный ид, ожидание пустого массива
        this.mockMvc.perform(get(URL, idIncorrect))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

}


