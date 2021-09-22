package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;

import java.util.Optional;

public interface ReputationService extends ReadWriteService<Reputation, Long> {
    Optional<Reputation> getByAuthorAndSenderAndQuestionAndType(User author, User sender, Question question, ReputationType reputationType);
    Optional<Reputation> getByAnswerIdSenderId(Long answerId,Long senderId);

}
