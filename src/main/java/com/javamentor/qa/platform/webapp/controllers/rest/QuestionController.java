package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import com.javamentor.qa.platform.service.abstracts.dto.CommentDtoService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<?> getAllCommentsOnQuestion(@PathVariable(value = "id") Long questionId) {
        if (questionService.existsById(questionId)) {
            List<QuestionCommentDto> comments = commentDtoService.getAllQuestionCommentDtoById(questionId);
            if (comments.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(comments);
            }
        }

        return ResponseEntity.badRequest().body("Question with id: " + questionId + " not found");
    }


    @GetMapping("/count")
    public ResponseEntity<?> getCountQuestion() {
        return new ResponseEntity<>(
                questionService.getCountQuestion(),
                HttpStatus.OK
        );
    }
}
