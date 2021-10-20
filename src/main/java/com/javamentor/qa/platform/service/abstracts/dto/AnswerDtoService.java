package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoService {

    List<AnswerDto> getAllAnswerDtoByQuestionId(Long id, Long userId);

    Optional<AnswerDto> getAnswerDtoById(Long answerId, Long userId);

    void updateAnswer (Long answerId, AnswerDto answerDto);
}
