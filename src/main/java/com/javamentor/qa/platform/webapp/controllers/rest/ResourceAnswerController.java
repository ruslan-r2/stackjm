package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user/question/{questionId}/answer")
public class ResourceAnswerController {

    private final AnswerDtoService answerDtoService;

    public ResourceAnswerController(AnswerDtoService answerDtoService) {
        this.answerDtoService = answerDtoService;
    }


    @GetMapping
    public List<AnswerDto> getAllAnswers(@PathVariable("questionId") Long id ){
        return answerDtoService.getAllAnswerDtoByQuestionId(id);
    }


}
