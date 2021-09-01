package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Resource Answer Controller", description = "Api для ответов на вопрос")
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private AnswerConverter answerConverter;

    private final AnswerService answerService;
    private final QuestionService questionService;

    @Autowired
    public ResourceAnswerController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    @Operation(summary = "Ответ на вопрос", description = "Позволяет добавить ответ на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@RequestBody AnswerDto answerDto,
                                                         @PathVariable @Parameter(description = "Идентификатор вопроса")
                                                                 Long questionId) {

        Answer answer = answerConverter.answerDtoToAnswer(answerDto);
        Question question = questionService.getById(questionId).orElseGet(Question::new);
        answer.setQuestion(question);

        if (!questionService.getById(questionId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(answerConverter.answerToAnswerDto(answer), HttpStatus.OK);
    }
}
