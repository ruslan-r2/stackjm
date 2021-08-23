package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface UserDao extends ReadWriteDao<User, Long> {
    User getUserByToken(String token);
}
