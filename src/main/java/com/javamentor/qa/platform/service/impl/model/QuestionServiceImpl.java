package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements ReadWriteService<Question, Long> {

    ReadWriteDao dao;

    @Autowired
    public QuestionServiceImpl(@Qualifier("questionDaoImpl") ReadWriteDao<Question, Long> readWriteDao) {
        super(readWriteDao);
        this.dao = readWriteDao;
    }
}
