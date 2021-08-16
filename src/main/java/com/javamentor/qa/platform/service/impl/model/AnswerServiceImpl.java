package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements ReadWriteService<Answer, Long> {

    ReadWriteDao dao;

    @Autowired
    public AnswerServiceImpl(@Qualifier("answerDaoImpl") ReadWriteDao<Answer, Long> readWriteDao) {
        super(readWriteDao);
        this.dao = readWriteDao;
    }
}
