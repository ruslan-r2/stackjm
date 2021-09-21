package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;

public interface ReputationService extends ReadWriteService<Reputation,Long>{

    void changeRep(Long answerId, User user, Integer count);
    Integer getRepByUserId(Long id);
}
