package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerDtoServiceImpl implements AnswerDtoService {
    private final AnswerDtoDao answerDtoDao;
    private final AnswerDao answerDao;
    private final AnswerConverter answerConverter;

    @Autowired
    public AnswerDtoServiceImpl(AnswerDtoDao answerDtoDao, AnswerDao answerDao, AnswerConverter answerConverter) {
        this.answerDtoDao = answerDtoDao;
        this.answerDao = answerDao;
        this.answerConverter = answerConverter;
    }

    @Override
    public List<AnswerDto> getAllAnswerDtoByQuestionId(Long id) {
        return answerDtoDao.getAllAnswersByQuestionId(id);
    }

    @Override
    public Optional<AnswerDto> getAnswerDtoById(Long answerId) { return answerDtoDao.getAnswerDtoById(answerId);
    }

    @Override
    public void updateAnswer(Long answerId, AnswerDto answerDto) {
        answerDao.updateAnswerSpecial(answerId, answerConverter.answerDtoToAnswer(answerDto));
    }
}
