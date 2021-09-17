package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Answers контроллер", description = "Api для работы с Answers")
@RequestMapping("api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerDtoService answerDtoService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    public ResourceAnswerController(AnswerDtoService answerDtoService, QuestionService questionService, AnswerService answerService) {
        this.answerDtoService = answerDtoService;
        this.questionService = questionService;
        this.answerService = answerService;
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



    @Operation(summary = "Комментарий к ответу", description = "Позволяет добавить комментарий к ответу на вопрос")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "400", description = "Ответ не найден")
    @PostMapping("/{answerId}/comment")
    public ResponseEntity<CommentAnswerDto> addCommentToAnswer(String comment, Long answerId) {
        if (answerService.getById(answerId).isPresent()) {

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
