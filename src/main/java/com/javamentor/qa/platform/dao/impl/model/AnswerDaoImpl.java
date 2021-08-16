package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements ReadWriteDao<Answer, Long> {

}
