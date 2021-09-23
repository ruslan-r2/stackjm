package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Answers контроллер", description = "Api для работы с Answers")
@RequestMapping("api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VoteAnswerService voteAnswerService;
    private final AnswerDtoService answerDtoService;

    public ResourceAnswerController(QuestionService questionService, AnswerService answerService, VoteAnswerService voteAnswerService, AnswerDtoService answerDtoService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.voteAnswerService = voteAnswerService;
        this.answerDtoService = answerDtoService;
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

    @PostMapping("/{id}/upVote")
    @Operation(summary = "Увеличивает оценку ответа")
    @ApiResponse(responseCode = "200", description = "Оценка ответа увеличена, репутация автора повышена")
    @ApiResponse(responseCode = "400", description = "Ответ не найден")
    public ResponseEntity<Long> upVote(@Parameter(description = "id ответа для поднятие оценки")@PathVariable("id") Long answerId,
                                       @AuthenticationPrincipal User user){
        Optional<Answer> answer = answerService.getById(answerId);
        if(answer.isPresent() && !answer.get().getUser().equals(user)){
            Long count = voteAnswerService.voteUp(answer.get(),user);
            return new ResponseEntity<>(count,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/downVote")
    @Operation(summary = "Уменьшает оценку ответа")
    @ApiResponse(responseCode = "200", description = "Оценка ответа уменьшена, репутация автора понижена")
    @ApiResponse(responseCode = "400", description = "Ответ не найден")
    public ResponseEntity<Long> downVote(@Parameter(description = "id ответа для снижения оценки")@PathVariable("id") Long answerId,
                                         @AuthenticationPrincipal User user ){
        Optional<Answer> answer = answerService.getAnswerForVote(answerId,user.getId());
        if(answer.isPresent()){
            Long count = voteAnswerService.voteDown(answer.get(),user);
            return new ResponseEntity<>(count,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
