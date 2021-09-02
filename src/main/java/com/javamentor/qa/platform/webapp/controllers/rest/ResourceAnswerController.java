package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/question/{questionId}/answer")
@AllArgsConstructor
public class ResourceAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(name = "questionId") Long questionId,
                                              @PathVariable(name = "answerId") Long answerId) {
        try {
            answerService.deleteById(answerId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
