package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;

import java.util.List;

public interface TagDtoDao {
    List<RelatedTagDto> getTopTags();
}
