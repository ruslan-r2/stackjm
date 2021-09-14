package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.question.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.question.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
@Schema(description = "Converter Question <-> QuestionCreateDto")
public abstract class QuestionConverter {

    public abstract Question questionCreateDtoToQuestion(QuestionCreateDto questionCreateDto);

    public abstract QuestionCreateDto questionToQuestionCreateDto(Question question);

    public abstract Question questionDtoToQuestion(QuestionDto questionDto);

    @InheritInverseConfiguration
    public abstract QuestionDto questionToQuestionDto(Question question);
}
