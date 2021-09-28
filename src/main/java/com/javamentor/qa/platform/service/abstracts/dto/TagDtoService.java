package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.IgnoredTagDto;

import java.util.List;

public interface TagDtoService {
    List<IgnoredTagDto> getIgnoredTagsByUserId(Long id);
}
