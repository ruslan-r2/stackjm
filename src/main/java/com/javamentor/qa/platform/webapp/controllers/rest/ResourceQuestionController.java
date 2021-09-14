package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("api/user/question")
public class ResourceQuestionController {

    private final QuestionDtoService questionDtoService;

    public ResourceQuestionController(QuestionDtoService questionDtoService) {
        this.questionDtoService = questionDtoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getById(@PathVariable(value = "id") Long id){
        Optional<QuestionDto> question = questionDtoService.getById(id);
        return question.map(questionDto -> new ResponseEntity<>(questionDto, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
