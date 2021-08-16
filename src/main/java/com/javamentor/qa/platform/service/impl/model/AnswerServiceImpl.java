package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.impl.model.AnswerDaoImpl;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements ReadWriteService<Answer, Long> {

    private AnswerDaoImpl answerDao;

    @Autowired
    public AnswerServiceImpl(AnswerDaoImpl answerDao) {
        super(answerDao);
        this.answerDao = answerDao;
    }
}
