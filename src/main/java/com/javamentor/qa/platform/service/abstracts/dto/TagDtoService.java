package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;

import java.util.List;

public interface TagDtoService {
    List<RelatedTagDto> getTopTags();
    List<IgnoredTagDto> getIgnoredTagsByUserId(Long id);
    List<TrackedTagDto> getTrackedByUserId(Long id);
    List<TagDto> getTop3TagsByUserId(Long id);
    List<Long> getTrackedIdsByUserId(Long id);
    List<Long> getIgnoredIdsByUserId(Long id);
}
