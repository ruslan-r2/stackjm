package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer,Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long sumVote(Long answerId) {
        Query query = entityManager.createQuery("select COALESCE(SUM(v.vote), 0) as count from VoteAnswer v  where v.answer.id = :id")
                .setParameter("id",answerId);
        return (Long) query.getSingleResult();
    }
}
