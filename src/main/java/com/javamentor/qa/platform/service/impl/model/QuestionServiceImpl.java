package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.impl.model.QuestionDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements ReadWriteService<Question, Long> {

    QuestionDaoImpl questionDao;

    @Autowired
    public QuestionServiceImpl(QuestionDaoImpl questionDao) {
        super(questionDao);
        this.questionDao = questionDao;
    }
}
