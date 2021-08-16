package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionDaoImpl extends ReadWriteDaoImpl<Question, Long> implements ReadWriteDao<Question, Long> {

}
