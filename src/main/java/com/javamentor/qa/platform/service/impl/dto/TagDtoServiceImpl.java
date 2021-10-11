package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagDtoServiceImpl implements TagDtoService {

    private final TagDtoDao tagDtoDao;

    @Autowired
    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<IgnoredTagDto> getIgnoredTagsByUserId(Long id) {
        return tagDtoDao.getIgnoredTagsByUserId(id);
    }

    @Override
    public List<TrackedTagDto> getTrackedByUserId(Long id) {
        return tagDtoDao.getTrackedByUserId(id);
    }

    @Override
    public List<RelatedTagDto> getTopTags() {
        return tagDtoDao.getTopTags();
    }

    @Override
    public List<Long> getTrackedIdsByUserId(Long id) {
       return getTrackedByUserId(id).stream().map(TrackedTagDto::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getIgnoredIdsByUserId(Long id) {
       return getIgnoredTagsByUserId(id).stream().map(IgnoredTagDto::getId).collect(Collectors.toList());
    }
}
