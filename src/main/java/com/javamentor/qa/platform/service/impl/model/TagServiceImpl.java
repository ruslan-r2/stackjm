package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.impl.model.TagDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;

public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements ReadWriteService<Tag, Long> {

    TagDaoImpl tagDao;

    @Autowired
    public TagServiceImpl(TagDaoImpl tagDao) {
        super(tagDao);
        this.tagDao = tagDao;
    }
}
