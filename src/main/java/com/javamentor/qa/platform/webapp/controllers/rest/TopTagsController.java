package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.webapp.converters.TagConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController("/api/user/tag/related")
public class TopTagsController {

    TagService tagService;

    TagConverter tagConverter;

    public TopTagsController(TagService tagService, TagConverter tagConverter) {
        this.tagService = tagService;
        this.tagConverter = tagConverter;
    }

    @GetMapping
    public ArrayList<RelatedTagDto> getTop10Tags() {
        List<RelatedTagDto> list = new ArrayList<>();
        for (Tag tag :
                tagService.getAll()) {
            list.add(tagConverter.tagToRelatedTagDto(tag));
        }
        list.sort((o1, o2) -> (int) (o1.getCountQuestion() - o2.getCountQuestion()));
        return (ArrayList<RelatedTagDto>) list.subList(0, 10);
    }

}
