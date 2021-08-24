package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @Autowired
    public ResourceAnswerController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@PathVariable Long questionId) {
        //Question question = questionService.getById(questionId).get();
        List<Answer> listAnswers = answerService.getAll();
        Optional<Question> question = questionService.getById(questionId);
        question.get().setAnswers(listAnswers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
