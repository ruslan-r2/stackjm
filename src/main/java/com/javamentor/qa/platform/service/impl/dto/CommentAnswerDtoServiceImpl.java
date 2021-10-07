package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.CommentAnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentAnswerDtoServiceImpl implements CommentAnswerDtoService {

    private final CommentAnswerDtoDao commentAnswerDtoDao;

    @Autowired
    public CommentAnswerDtoServiceImpl(CommentAnswerDtoDao commentAnswerDtoDao) {
        this.commentAnswerDtoDao = commentAnswerDtoDao;
    }

    @Override
    public CommentAnswerDto getCommentAnswerDtoById(Long answerId) {
        return commentAnswerDtoDao.getCommentAnswerDtoById(answerId);
    }
}
