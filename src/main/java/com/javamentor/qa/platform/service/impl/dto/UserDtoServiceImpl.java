package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserDtoServiceImpl extends PaginationServiceAbstract<UserDto> implements UserDtoService {

    private final UserDtoDao userDtoDao;
    private final TagDtoDao tagDtoDao;

    public UserDtoServiceImpl(Map<String, PaginationDao<UserDto>> paginationDaoMap, UserDtoDao userDtoDao, TagDtoDao tagDtoDao) {
        super(paginationDaoMap);
        this.userDtoDao = userDtoDao;
        this.tagDtoDao = tagDtoDao;
    }

    @Override

    public Optional<UserDto> getById(Long id) {
        Optional<UserDto> userDto = userDtoDao.getById(id);
        userDto.ifPresent(dto -> dto.setListTop3TagDto(tagDtoDao.getTop3TagsByUserId(id)));
        return userDto;
    }

    @Override
    public PageDto<UserDto> getPageDto(Map<String, Object> parameters) {
        return super.getPageDto(parameters);
    }

}
