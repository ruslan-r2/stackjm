package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/user/question/{questionId}/answer")
@AllArgsConstructor
public class ResourceAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @DeleteMapping("/{answerId}")
    public ResponseEntity<Answer> deleteAnswerById(@PathVariable(name = "questionId") Long questionId,
                                                 @PathVariable(name = "answerId") Long answerId) {
        answerService.deleteById(answerId);
        return new ResponseEntity<>(answerService.getById(answerId).get(), HttpStatus.OK);
    }
}
