package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.question.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@Schema(description = "Converter Tag <-> TagDto")
public abstract class TagConverter {

    public abstract Tag tagDtoToTag(TagDto tagDto);

    @InheritInverseConfiguration
    public abstract TagDto tagToTagDto(Tag tag);
}
