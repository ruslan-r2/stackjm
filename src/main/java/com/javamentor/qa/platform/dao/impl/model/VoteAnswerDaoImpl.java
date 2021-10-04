package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteAnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class VoteAnswerDaoImpl extends ReadWriteDaoImpl<VoteAnswer,Long> implements VoteAnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long sumVote(Long answerId) {
        Query query = entityManager
                .createQuery("select count(v.voteType) as count from VoteAnswer v  where v.answer.id = :id")
                .setParameter("id",answerId);
        return (Long) query.getSingleResult();
    }

    @Override
    public Optional<VoteAnswer> getByAnswerIdAndUserId(Long answerId, Long userId) {
        Query query = entityManager
                .createQuery("select v from VoteAnswer v where v.answer.id = :ansId and v.user.id = :userId")
                .setParameter("ansId",answerId)
                .setParameter("userId",userId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
