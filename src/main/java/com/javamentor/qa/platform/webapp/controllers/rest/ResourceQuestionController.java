package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.exception.VoteException;
import com.javamentor.qa.platform.models.dto.*;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import com.javamentor.qa.platform.webapp.converters.QuestionConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("api/user/question")
@Tag(name = "Question контроллер", description = "Api для работы с вопросами")
public class ResourceQuestionController {

    private final QuestionDtoService questionDtoService;
    private QuestionService questionService;
    private QuestionConverter questionConverter;
    private VoteQuestionService voteQuestionService;
    private ReputationService reputationService;

    public ResourceQuestionController(QuestionDtoService questionDtoService, QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService, QuestionConverter questionConverter) {
        this.questionDtoService = questionDtoService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
        this.questionConverter = questionConverter;
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
        }
        if (optionalQuestion.get().getUser().equals(user)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Question question = optionalQuestion.get();

        try {
            voteQuestionService.upVote(user, question);
        } catch (VoteException ve) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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
        }
        Question question = optionalQuestion.get();

        try {
            voteQuestionService.downVote(user, question);
        } catch (VoteException ve) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long sumVote = voteQuestionService.getSumVoteForQuestion(question);
        return new ResponseEntity<>(sumVote, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Добавляет новый вопрос, возвращает QuestionDto")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
                                        description = "Ответ успешно добавлен",
                                        content = @Content(mediaType = "application/json",
                                                           schema = @Schema(implementation = QuestionDto.class))),
                           @ApiResponse(responseCode = "400",
                                        description = "Ответ не добавлен, проверьте обязательные поля")})
    @ResponseBody
    public ResponseEntity<QuestionDto> addNewQuestion(@Parameter(description = "DTO создаваемого вопроса")
                                                      @Valid
                                                      @RequestBody
                                                      QuestionCreateDto questionCreateDto) {

        Question question = questionConverter.questionCreateDtotoEntity(questionCreateDto);
        question.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        questionService.persist(question);

        QuestionDto questionDto = questionConverter.entityToQuestionDto(question);
        return ResponseEntity.ok(questionDto);
    }

    @GetMapping("/noAnswer")
    public ResponseEntity<PageDto<QuestionDto>> getQuestionsWithoutAnswers (
            @Parameter(description = "номер страницы", required = true)
            @RequestParam(value = "currentPage") Integer currentPage,
            @Parameter(description = "кол-во элементов на странице", required = false)
            @RequestParam(value = "itemsOnPage", required = false, defaultValue = "10") Integer itemsOnPage,
            @Parameter(description = "список отслеживаемых тегов", required = false)
            @RequestParam(value = "trackedTags", required = false) List<TrackedTagDto> trackedTags,
            @Parameter(description = "список игнорируемых тегов", required = false)
            @RequestParam(value = "ignoredTags", required = false) List<IgnoredTagDto> ignoredTags) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", currentPage);
        parameters.put("itemsOnPage", itemsOnPage);
        parameters.put("hasAnswers", false);
        parameters.put("trackedTags", trackedTags);
        parameters.put("ignoredTags", ignoredTags);
        parameters.put("workPagination", "allQuestions");
        PageDto<QuestionDto> pageDto = questionDtoService.getPageDto(parameters);
        return ResponseEntity.ok(pageDto);
    }
}
