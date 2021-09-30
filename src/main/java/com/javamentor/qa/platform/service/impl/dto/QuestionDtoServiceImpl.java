package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionDtoServiceImpl implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;
    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao) {
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }


    @Override
    public Optional<QuestionDto> getById(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getById(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getByQuestionId(id)));
        return questionDto;
    }

}
