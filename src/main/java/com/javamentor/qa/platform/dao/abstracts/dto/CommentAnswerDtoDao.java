package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentAnswerDto;

public interface CommentAnswerDtoDao {

    CommentAnswerDto getCommentAnswerDtoById(Long answerId);
}
