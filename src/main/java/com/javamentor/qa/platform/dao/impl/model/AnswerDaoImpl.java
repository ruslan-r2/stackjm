package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void deleteById(Long id) {
        String hql = "update Answer answer set answer.isDeleted = true where answer.id = :id";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }
}
