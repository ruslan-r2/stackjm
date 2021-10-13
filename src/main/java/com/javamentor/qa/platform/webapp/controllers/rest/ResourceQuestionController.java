package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.exception.VoteException;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import com.javamentor.qa.platform.service.abstracts.model.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("api/user/question")
@Tag(name = "Question контроллер", description = "Api для работы с вопросами")
public class ResourceQuestionController {

    private final QuestionDtoService questionDtoService;
    private final QuestionService questionService;
    private final QuestionConverter questionConverter;
    private final VoteQuestionService voteQuestionService;
    private final ReputationService reputationService;
    private final TagDtoService tagDtoService;

    public ResourceQuestionController(QuestionDtoService questionDtoService, QuestionService questionService, VoteQuestionService voteQuestionService, ReputationService reputationService, QuestionConverter questionConverter, TagDtoService tagDtoService) {
        this.questionDtoService = questionDtoService;
        this.questionService = questionService;
        this.voteQuestionService = voteQuestionService;
        this.reputationService = reputationService;
        this.questionConverter = questionConverter;
        this.tagDtoService = tagDtoService;
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
    @Operation(summary = "Возвращает страницу вопросов у которых отсутствуют ответы. " +
            "Учитываются отслеживаемые и игнорируемые теги пользователя ")
    @ApiResponse(responseCode = "200", description = "успешно",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PageDto.class)))
    @ApiResponse(responseCode = "500", description = "Ошибка при обработке запроса")
    public ResponseEntity<PageDto<QuestionDto>> getQuestionsWithoutAnswers (
            @Parameter(description = "номер страницы", required = true)
            @RequestParam(value = "page") Integer page,
            @Parameter(description = "кол-во элементов на странице")
            @RequestParam(value = "items", required = false, defaultValue = "10") Integer items,
            @AuthenticationPrincipal User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("currentPage", page);
        parameters.put("itemsOnPage", items);
        parameters.put("noAnswers", true);
        parameters.put("trackedTag", tagDtoService.getTrackedIdsByUserId(user.getId()));
        parameters.put("ignoredTag", tagDtoService.getIgnoredIdsByUserId(user.getId()));
        parameters.put("workPagination", "allQuestions");
        return ResponseEntity.ok(questionDtoService.getPageDto(parameters));
    }
}
