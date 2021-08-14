package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl  extends ReadWriteDaoImpl<User, Long> implements ReadWriteDao<User, Long> {

}