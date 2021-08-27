package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @Override
    public void deleteById(Long id) {
        Answer answerToDelete = null;
        if (super.getById(id).isPresent()) {
            answerToDelete = super.getById(id).get();
        }
        assert answerToDelete != null;
        answerToDelete.setIsDeleted(true);
    }
}
