package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
@Schema(description = "преобразование Answer в AnswerDto и обратно")
public abstract class AnswerConverter {

    public abstract Answer answerDtoToAnswer(AnswerDto answerDto);

    @InheritInverseConfiguration
    public abstract AnswerDto answerToAnswerDto(Answer answer);


}
