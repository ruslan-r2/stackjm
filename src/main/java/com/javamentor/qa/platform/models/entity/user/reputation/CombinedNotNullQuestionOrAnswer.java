package com.javamentor.qa.platform.models.entity.user.reputation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = ReputationValidator.class)
public @interface CombinedNotNullQuestionOrAnswer {
    String message() default "questionId or answerId is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}