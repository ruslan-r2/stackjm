package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
@Schema(description = "преобразование Answer в AnswerDto и обратно")
public abstract class AnswerConverter {

    @Mappings({
            @Mapping(target="id", source="id"),
            @Mapping(target="user.id", source="userId"),
            @Mapping(target="question.id", source="questionId"),
            @Mapping(target="htmlBody", source="body"),
            @Mapping(target="persistDateTime", source="persistDate"),
            @Mapping(target="dateAcceptTime", source="dateAccept")
    })
    public abstract Answer answerDtoToAnswer(AnswerDto answerDto);

    @InheritInverseConfiguration
    public abstract AnswerDto answerToAnswerDto(Answer answer);

}
