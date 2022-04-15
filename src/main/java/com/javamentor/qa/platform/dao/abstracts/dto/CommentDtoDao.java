package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.QuestionCommentDto;

import java.util.List;

public interface CommentDtoDao {
    List<QuestionCommentDto> getAllQuestionCommentDtoById(Long questionId);
}
