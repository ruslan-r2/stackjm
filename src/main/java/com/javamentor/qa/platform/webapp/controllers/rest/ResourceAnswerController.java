package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Resource Answer Controller", description = "Api для ответов на вопрос")
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    AnswerConverter answerConverter;

    private final QuestionService questionService;

    @Autowired
    public ResourceAnswerController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Ответ на вопрос", description = "Позволяет добавить ответ на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@RequestBody AnswerDto answerDto,
                                                         @PathVariable @Parameter(description = "Идентификатор вопроса")
                                                                 Long questionId) {

        Optional<Question> questionToBeAnswered = questionService.getById(questionId);

        Answer answer = answerConverter.answerDtoToAnswer(answerDto);
        List<Answer> listAnswer = new ArrayList<>();
        listAnswer.add(answer);

        questionToBeAnswered.ifPresent(question -> question.setAnswers(listAnswer));

        return new ResponseEntity<>(answerConverter.answerToAnswerDto(answer), HttpStatus.OK);
    }
}
