package com.javamentor.qa.platform.models.entity.user.reputation;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ReputationValidator implements ConstraintValidator<CombinedNotNullQuestionOrAnswer, Reputation> {

    @Override
    public void initialize(final CombinedNotNullQuestionOrAnswer combinedNotNull) {
    }

    @Override
    public boolean isValid(final Reputation reputation, final ConstraintValidatorContext context) {

        return (reputation.getQuestion() == null && reputation.getAnswer() != null)
                && (reputation.getType() == ReputationType.Answer || reputation.getType() == ReputationType.VoteAnswer)
                || (reputation.getQuestion() != null && reputation.getAnswer() == null)
                && (reputation.getType() == ReputationType.Question || reputation.getType() == ReputationType.VoteQuestion);
    }
}
