package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.javamentor.qa.platform.models.dto.AuthenticationRequestDTO;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestResourceAnswerController extends AbstractIntegrationTest {

    private String URL = "http://localhost:8080/api/user/question/{questionId}/answer";
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {"userResourceController/roles.yml", "userResourceController/users.yml", "userResourceController/tags.yml", "userResourceController/questions.yml", "userResourceController/answers.yml"},cleanBefore = true)
    public void getAllAnswers() throws Exception{
//        AnswerDto answerDto = new AnswerDto();
//        answerDto.setId(100L);
//        answerDto.setUserId(100L);
//        answerDto.setNickname("admin");
//        answerDto.setBody("text");
//        answerDto.setIsHelpful(true);
//        answerDto.setPersistDate(LocalDateTime.parse("1990-10-10 00:00:00"));

        AuthenticationRequestDTO correct = new AuthenticationRequestDTO("admin@mail.ru","admin");
        String json = objectMapper.writeValueAsString(correct);
        this.mockMvc.perform(
                get(URL,100).contentType(MediaType.APPLICATION_JSON).content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON.)
                .andExpect(jsonPath("$[0].id",is(100)))
                .andExpect(jsonPath("$[0].userId",is(100)))
                .andExpect(jsonPath("$[0].nickname",is("admin")))
                .andExpect(jsonPath("$[0].body",is("text")))
        ;
//                .andExpect(jsonPath("$[0].persistDate",is(answerDto.getPersistDate().toString())));
    }
}


