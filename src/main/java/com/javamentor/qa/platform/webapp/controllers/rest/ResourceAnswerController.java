package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@Tag(name = "Resource Answer Controller", description = "Api для ответов на вопрос")
@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerConverter answerConverter;

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @Autowired
    public ResourceAnswerController(QuestionService questionService, AnswerConverter answerConverter, AnswerService answerService, UserService userService) {
        this.questionService = questionService;
        this.answerConverter = answerConverter;
        this.answerService = answerService;
        this.userService = userService;
    }

    @Operation(summary = "Ответ на вопрос", description = "Позволяет добавить ответ на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "400", description = "Вопрос не найден")
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@RequestBody AnswerDto answerDto,
                                                         @PathVariable @Parameter(description = "Идентификатор вопроса")
                                                                 Long questionId) {
        Answer answer = answerConverter.answerDtoToAnswer(answerDto);
        Question question = questionService.getById(questionId).orElse(null);

        if (question == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        answer.setQuestion(question);
        answerService.persist(answer);


        return new ResponseEntity<>(answerConverter.answerToAnswerDto(answer), HttpStatus.OK);
    }
}
/*
 * */

/*if (questionService.getById(questionId).isPresent()) {
            Answer answer = answerConverter.answerDtoToAnswer(answerDto);
            answerService.persist(answer);

            return new ResponseEntity<>(answerConverter.answerToAnswerDto(answer), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/