package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping(value = "api/user/question")
public class QuestionController {

    private QuestionService questionService;
    private VoteQuestionService voteQuestionService;
    private ReputationService reputationService;

    public QuestionController(QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService) {
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
    }

    @Operation(summary = "голосование за вопрос")
    @ApiResponse(responseCode = "200", description = "возвращает сумму голосов 'за' и 'против'",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "вопроса с таким id не найден")
    @PostMapping(value = "/{questionId}/upVote")
    @ResponseBody
    public ResponseEntity<Long> upVote(@Parameter(description = "id вопроса, за который голосует пользователь", required = true) @PathVariable(value = "questionId") Long questionId, @AuthenticationPrincipal User user) {
        // получаем вопрос по id
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Question question = optionalQuestion.get();

        // удаляем старый vote
        voteQuestionService.getByUserAndQuestion(user, question)
                .ifPresent(voteQuestion -> voteQuestionService.delete(voteQuestion));

        // удаление поставленной репутации ранее(если есть)
        reputationService.getByAuthorAndSenderAndQuestionAndType(question.getUser(), user, question, ReputationType.VoteQuestion)
                .ifPresent(reputation -> reputationService.delete(reputation));

        //создаем новый vote
        voteQuestionService.persist(new VoteQuestion(user, question, 1));

        //добавление репутацию автору вопроса
        Reputation reputationAuthor = new Reputation();
        reputationAuthor.setPersistDate(LocalDateTime.now());
        reputationAuthor.setAuthor(question.getUser());
        reputationAuthor.setSender(user);
        reputationAuthor.setCount(10);
        reputationAuthor.setType(ReputationType.VoteQuestion);
        reputationAuthor.setQuestion(question);
        reputationService.persist(reputationAuthor);

        // нахождение суммы vote для вопроса
        Long sumVote = voteQuestionService.getSumVoteForQuestion(question);
        return new ResponseEntity<>(sumVote, HttpStatus.OK);
    }

    @Operation(summary = "голосование против вопроса")
    @ApiResponse(responseCode = "200", description = "возвращает сумму голосов 'за' и 'против'",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "404", description = "вопроса с таким id не найден")
    @PostMapping(value = "/{questionId}/downVote")
    @ResponseBody
    public ResponseEntity<Long> downVote(@Parameter(description = "id вопроса, против которого голосует пользователь", required = true) @PathVariable(value = "questionId") Long questionId, @AuthenticationPrincipal User user) {
        // получаем вопрос по id
        Optional<Question> optionalQuestion = questionService.getById(questionId);
        if (!optionalQuestion.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Question question = optionalQuestion.get();

        // удаляем старый vote
        voteQuestionService.getByUserAndQuestion(user, question)
                .ifPresent(voteQuestion -> voteQuestionService.delete(voteQuestion));

        // удаление поставленной репутации ранее(если есть)
        reputationService.getByAuthorAndSenderAndQuestionAndType(question.getUser(), user, question, ReputationType.VoteQuestion)
                .ifPresent(reputation -> reputationService.delete(reputation));

        //создаем новый vote
        voteQuestionService.persist(new VoteQuestion(user, question, -1));

        //добавление репутацию автору вопроса
        Reputation reputationAuthor = new Reputation();
        reputationAuthor.setPersistDate(LocalDateTime.now());
        reputationAuthor.setAuthor(question.getUser());
        reputationAuthor.setSender(user);
        reputationAuthor.setCount(-5);
        reputationAuthor.setType(ReputationType.VoteQuestion);
        reputationAuthor.setQuestion(question);
        reputationService.persist(reputationAuthor);

        // нахождение суммы vote для вопроса
        Long sumVote = voteQuestionService.getSumVoteForQuestion(question);
        return new ResponseEntity<>(sumVote, HttpStatus.OK);
    }


}
