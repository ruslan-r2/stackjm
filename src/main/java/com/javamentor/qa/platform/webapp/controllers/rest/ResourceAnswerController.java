package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/question/")
@AllArgsConstructor
public class ResourceAnswerController {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestionById(@PathVariable(name = "questionId") Long questionId) {
        Question result;
        if (questionDao.getById(questionId).isPresent()) {
            result = questionDao.getById(questionId).get();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Answer> deleteAnswerById(@PathVariable(name = "questionId") Long questionId,
                                                   @PathVariable(name = "answerId") Long answerId) {
        Question question = getQuestionById(questionId).getBody();
        assert question != null;
        List<Answer> answers = question.getAnswers();
        for (Answer answer : answers) {
            if (answer.getId().equals(answerId)) {
                answerDao.delete(answer);
                return new ResponseEntity<>(answer, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
