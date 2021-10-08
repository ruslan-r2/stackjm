package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionDtoServiceImpl extends PaginationServiceAbstract<QuestionDto> implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;
    private final TagDtoDao tagDtoDao;

    public QuestionDtoServiceImpl(Map<String, PaginationDao<QuestionDto>> paginationDaoMap,
                                  QuestionDtoDao questionDtoDao, TagDtoDao tagDtoDao) {
        super(paginationDaoMap);
        this.questionDtoDao = questionDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public PageDto<QuestionDto> getPageDto(Map<String, Object> parameters) {
        PageDto<QuestionDto> pageDto = super.getPageDto(parameters);
        List<QuestionDto> questionDtoList = pageDto.getItems();
        List<Long> questionsIdList = questionDtoList.stream().map(QuestionDto::getId).collect(Collectors.toList());
        List<TagDto> tagList = tagDtoDao.getTagsByQuestionIdList(questionsIdList);
        Map<Long, List<Long>> questionTagIdReferenceMap = questionDtoDao.getQuestionTagIdMap(questionsIdList);
        questionDtoList.forEach(list -> list.setListTagDto(new ArrayList<>((tagList.stream()
                .filter(tag -> questionTagIdReferenceMap.get(list.getId()).contains(tag.getId()))
                .collect(Collectors.toList())))));
        pageDto.setItems(questionDtoList);
        return pageDto;
    }

    @Override
    public Optional<QuestionDto> getById(Long id) {
        return questionDtoDao.getById(id);
    }
}
