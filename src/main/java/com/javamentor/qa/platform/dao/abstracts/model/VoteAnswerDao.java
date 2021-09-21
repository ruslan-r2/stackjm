package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;

public interface VoteAnswerDao extends ReadWriteDao<VoteAnswer,Long> {
    Long sumVote(Long answerId);
}
