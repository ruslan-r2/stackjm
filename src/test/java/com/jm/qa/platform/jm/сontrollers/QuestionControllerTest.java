package com.jm.qa.platform.jm.сontrollers;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jm.qa.platform.jm.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QuestionControllerTest extends AbstractIntegrationTest {


    // проверка получения комментариев к существующему вопросу(question)
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/comment.yml",
            "resource_question_controller/comment_question.yml",
            "resource_question_controller/answers.yml",
            "resource_question_controller/reputations.yml"
    }, cleanBefore = true)
    @Test
    public void getAllCommentQuestion() throws Exception {


        String token = getToken("test1@mail.ru", "user");

        mockMvc.perform(get("/api/user/question/111/comment")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].questionId").value(111))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("testComment 102 for question id = 111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].imageLink").value("https://www.kemri.org/wp-content/uploads/2020/01/495-4952535_create-digital-profile-icon-blue-user-profile-icon.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reputation").value(1500))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].questionId").value(111))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(103))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].text").value("testComment 103 for question id = 111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].imageLink").value("https://www.kemri.org/wp-content/uploads/2020/01/495-4952535_create-digital-profile-icon-blue-user-profile-icon.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reputation").value(800))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].questionId").value(111))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(104))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].text").value("testComment 104 for question id = 111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].userId").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imageLink").value("https://www.kemri.org/wp-content/uploads/2020/01/495-4952535_create-digital-profile-icon-blue-user-profile-icon.png"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].reputation").value(800));
    }

    // проверка получения комментариев к НЕ существующему вопросу(question)
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/comment.yml",
            "resource_question_controller/comment_question.yml",
            "resource_question_controller/answer.yml",
            "resource_question_controller/reputation.yml"
    }, cleanBefore = true)
    @Test
    public void getAllCommentQuestionNotFound() throws Exception {

        String token = getToken("test1@mail.ru", "user");

        mockMvc.perform(get("/api/user/question/901/comment")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    // проверка получения комментариев, если их нету
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
            "resource_question_controller/comment.yml",
            "resource_question_controller/comment_question.yml",
            "resource_question_controller/answer.yml",
            "resource_question_controller/reputation.yml"
    }, cleanBefore = true)
    @Test
    public void getEmptyListCommentQuestion() throws Exception {

        String token = getToken("test1@mail.ru", "user");

        mockMvc.perform(get("/api/user/question/901/comment")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    @DataSet(value = {"resource_question_controller/roles.yml",
            "resource_question_controller/users.yml",
            "resource_question_controller/questions.yml",
    }, cleanBefore = true)
    public void getCountQuestion() throws Exception {
        String token = getToken("test1@mail.ru", "user");

        String responseMessage = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/question/count")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseMessage).isEqualTo(Long.toString(3));

    }

    @Test
    @DataSet(value = {"resource_question_controller/users.yml", "resource_question_controller/roles.yml", "resource_question_controller/no_questions.yml"}, cleanBefore = true)
    public void getCountQuestionZero() throws Exception {
        String token = getToken("test1@mail.ru", "user");

        String responseMessage = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/question/count")
                .header("Authorization",  token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseMessage).isEqualTo(Long.toString(0));
    }


}
