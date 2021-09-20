package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@Schema(description = "преобразование Tag в RelatedTagDto и обратно")
public abstract class TagConverter {

    @Mapping(target = "title", source = "name")
    @Mapping(target = "countQuestion", expression = "java((long)tag.getQuestions().size())")
    public abstract RelatedTagDto tagToRelatedTagDto(Tag tag);

}
