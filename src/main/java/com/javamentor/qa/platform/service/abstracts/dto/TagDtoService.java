package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;

public interface TagDtoService {
    List<TagDto> getIgnoredTagsByUserId(Long id);
}
