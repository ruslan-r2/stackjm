package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Answers контроллер", description = "Api для работы с Answers")
@RequestMapping("api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerDtoService answerDtoService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


    @Autowired
    public ResourceAnswerController(AnswerDtoService answerDtoService, QuestionService questionService,
                                    AnswerService answerService, UserService userService) {
        this.answerDtoService = answerDtoService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Возвращает лист DTO ответов по id вопроса")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnswerDto.class)))
    @ApiResponse(responseCode = "400", description = "Вопроса по ID не существует")
    public ResponseEntity<List<AnswerDto>> getAllAnswers(@Parameter(description = "id вопроса по которому получим ответы") @PathVariable("questionId") Long id) {

        if (!questionService.getById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<AnswerDto> list = answerDtoService.getAllAnswerDtoByQuestionId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Ответ на вопрос", description = "Позволяет добавить ответ на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "400", description = "Вопрос не найден")
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@AuthenticationPrincipal User user, @RequestBody AnswerDto answerDto,
                                                         @PathVariable @Parameter(description = "Идентификатор вопроса")
                                                                 Long questionId) throws Exception {

        Answer answer = answerService.addAnswerOnQuestion(user, questionId);
        answerDto.setId(answer.getId());
        answerDto.setBody(answer.getHtmlBody());
        answerDto.setUserId(answer.getUser().getId());
        answerDto.setIsHelpful(answer.getIsHelpful());
        answerDto.setQuestionId(answer.getQuestion().getId());
        answerDto.setPersistDate(answer.getPersistDateTime());
        answerDto.setDateAccept(answer.getDateAcceptTime());
        answerDto.setCountUserReputation(answerDtoService.getAnswerDtoById(answer.getId()).getCountUserReputation());
        return new ResponseEntity<>(answerDto, HttpStatus.OK);
    }
}