package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.CommentAnswer;
import com.javamentor.qa.platform.models.entity.user.User;

public interface CommentAnswerService extends ReadWriteService<CommentAnswer, Long>{

    CommentAnswer addCommentToAnswer(User user, Long answerId, String comment);
}
