package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;
import java.util.Optional;

public interface AnswerDtoDao {

    List<AnswerDto> getAllAnswersByQuestionId(Long id, Long userId);

    Optional<AnswerDto> getAnswerDtoById(Long id, Long userId);
}
