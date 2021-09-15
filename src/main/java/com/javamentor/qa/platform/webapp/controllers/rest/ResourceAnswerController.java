package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
@AllArgsConstructor
public class ResourceAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @Operation(summary = "Удаление вопроса по Id")
    @ApiResponse(responseCode = "200", description = "Вопрос помечен на удаление")
    @ApiResponse(responseCode = "400", description = "Вопрос не найден")
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(name = "questionId") Long questionId,
                                              @PathVariable(name = "answerId") Long answerId) {
        if (answerService.getById(answerId).isPresent()) {
            answerService.deleteById(answerId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
