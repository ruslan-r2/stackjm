package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/question")
public class QuestionController {

    private final QuestionService questionService;
    private final CommentDtoService commentDtoService;


    public QuestionController(QuestionService questionService, CommentDtoService commentDtoService) {
        this.questionService = questionService;
        this.commentDtoService = commentDtoService;
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<?> getAllCommentsOnQuestion(

            @PathVariable(value = "id") Long questionId) {

        if (questionService.existsById(questionId)) {
            return ResponseEntity.ok(commentDtoService.getAllQuestionCommentDtoById(questionId));
        }

        return ResponseEntity.badRequest().body("Question with id: " + questionId + " not found");
    }
}
