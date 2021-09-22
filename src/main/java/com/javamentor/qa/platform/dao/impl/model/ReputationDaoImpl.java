package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
<<<<<<< HEAD
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation,Long> implements ReputationDao {
=======
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation, Long> implements ReputationDao {
>>>>>>> dev

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reputation> getByAuthorAndSenderAndQuestionAndType(User author, User sender, Question question, ReputationType reputationType) {
        TypedQuery<Reputation> typedQuery = entityManager.createQuery("FROM Reputation r WHERE r.author = :author and r.sender = :sender and r.question = :question and r.type = :reputationType", Reputation.class);
        typedQuery.setParameter("author", author);
        typedQuery.setParameter("sender", sender);
        typedQuery.setParameter("question", question);
        typedQuery.setParameter("reputationType", reputationType);
        return SingleResultUtil.getSingleResultOrNull(typedQuery);
    }
    @Override
    public Optional<Reputation> getByAnswerIdSenderId(Long answerId, Long senderId) {
        Query query = entityManager.createQuery("select r from Reputation r where r.answer.id = :ansId and r.sender.id = :senderId")
                .setParameter("ansId",answerId)
                .setParameter("senderId",senderId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
