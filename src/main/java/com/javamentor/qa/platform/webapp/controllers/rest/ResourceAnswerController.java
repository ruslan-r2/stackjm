package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/question/{questionId}/answer")
@AllArgsConstructor
public class ResourceAnswerController {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Answer> deleteAnswerById(@PathVariable(name = "questionId") Long questionId,
                                                   @PathVariable(name = "answerId") Long answerId) {
        Question question;
        if (questionDao.getById(questionId).isPresent()) {
            question = questionDao.getById(questionId).get();
            List<Answer> answers = question.getAnswers();
            for (Answer answer : answers) {
                if (answer.getId().equals(answerId)) {
                    answerDao.delete(answer);
                    return new ResponseEntity<>(answer, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
