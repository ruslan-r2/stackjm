package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ReadWriteServiceImpl <Role, Long> implements ReadWriteDao<Role, Long> {

    private ReadWriteDao readWriteDao;

    @Autowired
    public RoleServiceImpl(@Qualifier("roleDaoImpl") ReadWriteDao readWriteDao) {
        super(readWriteDao);
        this.readWriteDao = readWriteDao;
    }
}
