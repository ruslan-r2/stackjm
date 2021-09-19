package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface VoteQuestionService extends ReadWriteService<VoteQuestion, Long> {
    Optional<VoteQuestion> getByUserAndQuestion(User user, Question question);
    Long getSumVoteForQuestion(Question question);
}
