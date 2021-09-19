package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
@Schema(description = "преобразование Tag в RelatedTagDto и обратно")
public abstract class TagConverter {

    @Mapping(target = "title", source = "name")
    @Mapping(target = "countQuestion", expression = "java((long)tag.getQuestions().size())")
    public abstract RelatedTagDto tagToRelatedTagDto(Tag tag);

    public abstract TagDto TagToTagDto(Tag tag);

}
