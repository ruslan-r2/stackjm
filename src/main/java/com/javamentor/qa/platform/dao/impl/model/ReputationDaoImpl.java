package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation,Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reputation> getByAnswerIdSenderId(Long answerId, Long senderId) {
        Query query = entityManager.createQuery("select r from Reputation r where r.answer.id = :ansId and r.sender.id = :senderId")
                .setParameter("ansId",answerId)
                .setParameter("senderId",senderId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
