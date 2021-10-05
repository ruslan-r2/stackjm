package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoDao {

    List<AnswerDto> getAllAnswersByQuestionId(Long id);

    Optional<AnswerDto> getAnswerDtoById(Long answerId);

    void updateAnswer (Long answerId, AnswerDto answerDto);
}
