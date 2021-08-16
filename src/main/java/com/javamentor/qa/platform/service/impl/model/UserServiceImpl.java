package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ReadWriteServiceImpl <User, Long> implements ReadWriteService<User, Long> {

    private ReadWriteDao readWriteDao;

    @Autowired
    public UserServiceImpl(@Qualifier("userDaoImpl") ReadWriteDao readWriteDao) {
        super(readWriteDao);
        this.readWriteDao = readWriteDao;
    }
}