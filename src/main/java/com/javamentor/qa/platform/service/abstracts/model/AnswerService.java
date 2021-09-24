package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;


public interface AnswerService extends ReadWriteService<Answer, Long> {

    Answer addAnswerOnQuestion(User user, Long questionId, Answer answer);

}
