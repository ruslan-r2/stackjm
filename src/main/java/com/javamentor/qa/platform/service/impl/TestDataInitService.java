package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import com.javamentor.qa.platform.service.impl.model.RoleServiceImpl;
import com.javamentor.qa.platform.service.impl.model.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataInitService {


    private UserServiceImpl userService;
    private RoleServiceImpl roleService;

    @Autowired
    public TestDataInitService(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Transactional
    public void createUser(User user) {
        userService.persist(user);
    }

    @Transactional
    public void createRole(Role role) {
        roleService.persist(role);
    }
}
