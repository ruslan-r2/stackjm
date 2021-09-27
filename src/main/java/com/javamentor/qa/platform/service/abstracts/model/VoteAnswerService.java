package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;

public interface VoteAnswerService extends ReadWriteService<VoteAnswer,Long>{
    Long voteUp(Answer answer, User user);
    Long voteDown(Answer answer, User user);
}
