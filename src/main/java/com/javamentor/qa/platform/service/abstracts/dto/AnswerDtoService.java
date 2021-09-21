package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;
import java.util.List;

public interface AnswerDtoService {

    List<AnswerDto> getAllAnswerDtoByQuestionId(Long id);

    AnswerDto getAnswerDtoById(Long questionId, Long answerId);
}
