package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Answers контроллер", description = "Api для работы с Answers")
@RequestMapping("api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerDtoService answerDtoService;
    private final QuestionService questionService;
    private final ReputationService reputationService;
    private final AnswerService answerService;

    public ResourceAnswerController(AnswerDtoService answerDtoService, QuestionService questionService, ReputationService reputationService, AnswerService answerService) {
        this.answerDtoService = answerDtoService;
        this.questionService = questionService;
        this.reputationService = reputationService;
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

    @PostMapping("{id}/upVote")
    public ResponseEntity<Integer> upVote(@PathVariable("questionId") Long questionId,@PathVariable("id") Long answerId,@AuthenticationPrincipal User user){
        reputationService.changeRep(answerId,user,10);
        User user1 = questionService.getById(questionId).get().getUser();
        return new ResponseEntity<>(reputationService.getRepByUserId(user1.getId()),HttpStatus.OK);
    }

    @PostMapping("{id}/downVote")
    public ResponseEntity<Integer> downVote(@PathVariable("questionId") Long questionId,@PathVariable("id") Long answerId,@AuthenticationPrincipal User user ){
        reputationService.changeRep(answerId,user,-5);
        User user1 = questionService.getById(questionId).get().getUser();
        return new ResponseEntity<>(reputationService.getRepByUserId(user1.getId()),HttpStatus.OK);
    }


}
