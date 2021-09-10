package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.service.abstracts.dto.UserDtoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserDtoServiceImpl extends PaginationServiceAbstract<UserDto> implements UserDtoService {

    private final UserDtoDao userDtoDao;

    public UserDtoServiceImpl(Map<String, PaginationDao<UserDto>> paginationDaoMap, UserDtoDao userDtoDao) {
        super(paginationDaoMap);
        this.userDtoDao = userDtoDao;
    }

    @Override

    public Optional<UserDto> getById(Long id) {
        return userDtoDao.getById(id);
    }

    @Override
    public PageDto<UserDto> getPageDto(Map<String, Object> parameters) {
        return super.getPageDto(parameters);
    }
}
