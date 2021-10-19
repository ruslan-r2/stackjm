package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;

import java.util.List;
import java.util.Map;

public interface TagDtoDao {
    List<RelatedTagDto> getTopTags();
    List<TagDto> getByQuestionId(Long id);
    Map<Long,List<TagDto>> getTagsByQuestionIdList (List<Long> questionIdList);
    List<IgnoredTagDto> getIgnoredTagsByUserId(Long id);
    List<TrackedTagDto> getTrackedByUserId(Long id);
    List<TagDto> getTop3TagsByUserId(Long id);
}
