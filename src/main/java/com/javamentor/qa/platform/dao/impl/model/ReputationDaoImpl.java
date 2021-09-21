package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class ReputationDaoImpl extends ReadWriteDaoImpl<Reputation,Long> implements ReputationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer getRepByUserId(Long id) {
        Query query = entityManager.createQuery("select COALESCE(SUM(r.count), 0) as count from Reputation r  where r.author.id = :id")
                .setParameter("id", id);
        return Integer.parseInt(query.getResultList().get(0).toString());
    }
}
