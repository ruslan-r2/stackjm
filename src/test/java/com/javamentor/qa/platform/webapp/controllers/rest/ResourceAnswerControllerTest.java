package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.impl.model.AnswerDaoImpl;
import com.javamentor.qa.platform.dao.impl.model.QuestionDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class ResourceAnswerControllerTest {

    @Test
    void getQuestionById(Long id) {
        ResourceAnswerController controller = new ResourceAnswerController(new QuestionDaoImpl(), new AnswerDaoImpl());
        Question response = controller.getQuestionById(id).getBody();
        assert response != null;
        assertEquals(response.getId(), id);
    }

    @Test
    void deleteAnswerById(Long questionId, Long answerId) {
        ResourceAnswerController controller = new ResourceAnswerController(new QuestionDaoImpl(), new AnswerDaoImpl());
        Answer response = controller.deleteAnswerById(questionId, answerId).getBody();
        assert response != null;
        assertEquals(response.getQuestion().getId(), questionId);
        assertEquals(response.getId(), answerId);
    }
}