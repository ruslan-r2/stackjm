package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class QuestionDtoServiceImpl extends PaginationServiceAbstract<QuestionDto> implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;
    public QuestionDtoServiceImpl(Map<String, PaginationDao<QuestionDto>> paginationDaoMap,
                                  QuestionDtoDao questionDtoDao,
                                  TagDtoDao tagDtoDao) {
        super(paginationDaoMap);
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public Optional<QuestionDto> getById(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getById(id);
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getByQuestionId(id)));
        return questionDto;
    }

    @Override
    public PageDto<QuestionDto> getPageDto(Map<String, Object> parameters) {
        return super.getPageDto(parameters);
    }

}
