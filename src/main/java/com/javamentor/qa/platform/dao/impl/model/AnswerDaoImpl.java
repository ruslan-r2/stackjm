package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    private EntityManager entityManager;

    @Autowired
    public AnswerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void deleteById(Long id) {
        String hql = "FROM Answer WHERE id = :id SET is_deleted = true";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }
}
