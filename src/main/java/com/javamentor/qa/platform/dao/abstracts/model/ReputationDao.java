package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

import java.util.Optional;

public interface ReputationDao extends ReadWriteDao<Reputation,Long>{
    Optional<Reputation> getByAnswerIdSenderId(Long answerId, Long senderId);
}
