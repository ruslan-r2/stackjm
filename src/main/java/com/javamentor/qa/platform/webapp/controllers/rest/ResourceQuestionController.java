package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;


@RestController
@RequestMapping("api/user/question")
@Tag(name = "Question контроллер", description = "Api для работы с вопросами")
public class ResourceQuestionController {

    private final QuestionDtoService questionDtoService;
    private QuestionService questionService;
    private VoteQuestionService voteQuestionService;
    private ReputationService reputationService;

    public ResourceQuestionController(QuestionDtoService questionDtoService,QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService) {
        this.questionDtoService = questionDtoService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Возвращает DTO вопроса по Id")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = QuestionDto.class)))
    @ApiResponse(responseCode = "400", description = "Вопроса по ID не существует")
    public ResponseEntity<QuestionDto> getById(@Parameter(description = "id вопроса ") @PathVariable(value = "id") Long id) {
        Optional<QuestionDto> questionDto = questionDtoService.getById(id);
        return questionDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "голосование за вопрос")
    @ApiResponse(responseCode = "200", description = "возвращает сумму голосов 'за' и 'против'",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "вопроса с таким id не найден")
    @ApiResponse(responseCode = "400", description = "пользователь голосует за свой вопрос")
    @PostMapping(value = "/{questionId}/upVote")
    @ResponseBody
    public ResponseEntity<Long> upVote(@Parameter(description = "id вопроса, за который голосует пользователь", required = true) @PathVariable(value = "questionId") Long questionId, @AuthenticationPrincipal User user) {
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (optionalQuestion.get().getUser().equals(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Question question = optionalQuestion.get();

        voteQuestionService.upVote(user, question);

        Long sumVote = voteQuestionService.getSumVoteForQuestion(question);
        return new ResponseEntity<>(sumVote, HttpStatus.OK);
    }

    @Operation(summary = "голосование против вопроса")
    @ApiResponse(responseCode = "200", description = "возвращает сумму голосов 'за' и 'против'",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "вопроса с таким id не найден")
    @ApiResponse(responseCode = "400", description = "пользователь голосует за свой вопрос")
    @PostMapping(value = "/{questionId}/downVote")
    @ResponseBody
    public ResponseEntity<Long> downVote(@Parameter(description = "id вопроса, против которого голосует пользователь", required = true) @PathVariable(value = "questionId") Long questionId, @AuthenticationPrincipal User user) {
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (optionalQuestion.get().getUser().equals(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Question question = optionalQuestion.get();

        voteQuestionService.downVote(user, question);

        Long sumVote = voteQuestionService.getSumVoteForQuestion(question);
        return new ResponseEntity<>(sumVote, HttpStatus.OK);
    }
}
