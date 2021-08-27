package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void deleteById(Long id) {
        Class<Answer> clazz = (Class<Answer>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        String hql = "from " + clazz.getName() + " WHERE id = :id set is_deleted = true";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }
}
