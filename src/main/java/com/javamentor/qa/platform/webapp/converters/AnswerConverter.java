package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.mapstruct.Mapper;

import java.util.Optional;


@Mapper
public abstract class AnswerConverter {

    public abstract Answer answerDtoToAnswer(AnswerDto answerDto);

    public abstract AnswerDto answerToAnswerDto(Answer answer);


}
