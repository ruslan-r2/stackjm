package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class VoteQuestionDaoImpl extends ReadWriteDaoImpl<VoteQuestion, Long> implements VoteQuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<VoteQuestion> getByUserAndQuestion(User user, Question question) {
        TypedQuery<VoteQuestion> typedQuery = entityManager.createQuery("select vq from VoteQuestion vq WHERE vq.question.id = :questionId AND vq.user.id = :userId", VoteQuestion.class);
        typedQuery.setParameter("userId", user.getId());
        typedQuery.setParameter("questionId", question.getId());
        return SingleResultUtil.getSingleResultOrNull(typedQuery);
    }

    @Override
    public Long getSumVoteForQuestion(Question question) {
        TypedQuery<Long> typedQuery =  entityManager.createQuery( "SELECT COALESCE(SUM(CASE WHEN vq.voteTypeQ = 'UP' THEN 1 WHEN vq.voteTypeQ = 'DOWN' THEN -1 END), 0) FROM VoteQuestion vq WHERE vq.question.id = :questionId", Long.class);
        typedQuery.setParameter("questionId", question.getId());
        return typedQuery.getSingleResult();
    }
}
