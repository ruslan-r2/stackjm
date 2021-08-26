package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    AnswerDto answerDto(Answer answer);
}
