package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

import java.util.Optional;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer,Long> {
    Long sumVote(Long answerId);
    Optional<VoteAnswer> getByAnswerIdAndUserId(Long answerId, Long userId);
}
