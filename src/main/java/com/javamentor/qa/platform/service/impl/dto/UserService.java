package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;

import java.util.Optional;

public interface UserService extends ReadWriteService<User, Long> {

    Optional<User> getByEmail (String email);

}
