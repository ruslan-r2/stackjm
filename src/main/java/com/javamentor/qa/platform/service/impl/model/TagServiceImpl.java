package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Qualifier;

public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements ReadWriteService<Tag, Long> {

    ReadWriteDao dao;

    public TagServiceImpl(@Qualifier("tagDaoImpl") ReadWriteDao<Tag, Long> readWriteDao) {
        super(readWriteDao);
        this.dao = readWriteDao;
    }
}
