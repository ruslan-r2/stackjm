package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;

public interface VoteAnswerService extends ReadWriteService<VoteAnswer,Long>{
    Long voteUp(Long answerId, User user);
    Long voteDown(Long answerId, User user);
}
