package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.CommentAnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.CommentAnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final CommentAnswerDtoService commentAnswerDtoService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentAnswerService commentAnswerService;
    private final AnswerConverter answerConverter;


    @Autowired
    public ResourceAnswerController(AnswerDtoService answerDtoService, CommentAnswerDtoService commentAnswerDtoService,
                                    QuestionService questionService, AnswerService answerService,
                                    CommentAnswerService commentAnswerService, AnswerConverter answerConverter) {
        this.answerDtoService = answerDtoService;
        this.commentAnswerDtoService = commentAnswerDtoService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.commentAnswerService = commentAnswerService;
        this.answerConverter = answerConverter;
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

    @DeleteMapping("/{answerId}")
    @Operation(summary = "Помечает ответ на удаление")
    @ApiResponse(responseCode = "200", description = "Вопрос успешно помечен на удаление")
    @ApiResponse(responseCode = "403", description = "Вопрос не найден")
    public ResponseEntity<?> markAnswerToDelete(@PathVariable("answerId") Long answerId) {
        if (answerService.getById(answerId).isPresent()) {
            answerService.deleteById(answerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Ответ на вопрос", description = "Позволяет добавить ответ на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "400", description = "Вопрос не найден")
    @PostMapping
    public ResponseEntity<AnswerDto> addAnswerToQuestion(@AuthenticationPrincipal User user, @RequestBody AnswerDto answerDto,
                                                         @PathVariable @Parameter(description = "Идентификатор вопроса")
                                                                 Long questionId) {

        Answer answerMakeFromDto = answerConverter.answerDtoToAnswer(answerDto);
        Answer answerOnQuestion = answerService.addAnswerOnQuestion(user, questionId, answerMakeFromDto);
        return new ResponseEntity<>(answerDtoService.getAnswerDtoById(answerOnQuestion.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Комментарий к ответу", description = "Позволяет добавить комментарий к ответу на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "400", description = "Ответ не найден")
    @PostMapping("/{answerId}/comment")
    public ResponseEntity<CommentAnswerDto> addCommentToAnswer(@AuthenticationPrincipal User user,
                                                               @Parameter(description = "комментарий который будет добавлен к ответу")
                                                               @RequestBody String comment,
                                                               @Parameter(description = "id ответа к которому добавляем комментарий")
                                                               @PathVariable("answerId") Long id) {

        CommentAnswer commentAnswer = commentAnswerService.addCommentToAnswer(user, id, comment);
        return new ResponseEntity<>(commentAnswerDtoService.getCommentAnswerDtoById(commentAnswer.getAnswer().getId()), HttpStatus.OK);
    }
}


