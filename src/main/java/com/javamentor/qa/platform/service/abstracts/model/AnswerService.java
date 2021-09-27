package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.answer.Answer;

import java.util.Optional;

public interface AnswerService extends ReadWriteService<Answer, Long> {

    Answer addAnswerOnQuestion(User user, Long questionId, Answer answer);

    Optional<Answer> getAnswerForVote(Long answerId,Long userId);

}
