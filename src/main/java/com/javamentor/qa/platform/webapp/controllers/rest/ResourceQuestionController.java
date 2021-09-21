package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("api/user/question")
@Tag(name = "Question контроллер", description = "Api для работы с вопросами")
public class ResourceQuestionController {

    private final QuestionDtoService questionDtoService;

    public ResourceQuestionController(QuestionDtoService questionDtoService) {
        this.questionDtoService = questionDtoService;
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
}
