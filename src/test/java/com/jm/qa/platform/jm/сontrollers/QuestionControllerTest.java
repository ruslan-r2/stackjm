package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QuestionControllerTest extends AbstractIntegrationTest {

    private String username;
    private String password;
    private String token;

    @Test
    @DataSet(value = {"question_controller/questions.yml", "question_controller/roles.yml", "question_controller/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void should_return_status_not_found() throws Exception {
        username = "user101@mail.ru";
        password = "user101";

        mockMvc.perform(post("/api/user/question/99/upVote").header("Authorization", getToken(username, password)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DataSet(value = {"question_controller/questions.yml", "question_controller/roles.yml", "question_controller/users.yml"}, cleanAfter = true, cleanBefore = true)
    public void testing_upVote_and_downVote() throws Exception {
        username = "user101@mail.ru";
        password = "user101";
        token = getToken(username, password);

        // положительный голос 101-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        // повторный положительный голос 101-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        // положительный голос 101-ого за другой вопрос
        mockMvc.perform(post("/api/user/question/101/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(1)));

        username = "user102@mail.ru";
        password = "user102";
        token = getToken(username, password);

        // положительный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/upVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(2)));

        // отрицательный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/downVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));

        // повторный отрицательный голос 102-ого
        mockMvc.perform(post("/api/user/question/100/downVote").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(0)));
    }
}
