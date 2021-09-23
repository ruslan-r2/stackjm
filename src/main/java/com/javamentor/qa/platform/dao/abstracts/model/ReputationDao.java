package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;

import java.util.Optional;

public interface ReputationDao extends ReadWriteDao<Reputation, Long> {
    Optional<Reputation> getByAuthorAndSenderAndQuestionAndType(User author, User sender, Question question, ReputationType reputationType);
}
