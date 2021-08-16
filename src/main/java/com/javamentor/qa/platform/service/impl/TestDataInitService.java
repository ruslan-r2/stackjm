package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.service.impl.dto.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataInitService {

    private UserService userService;

    @Autowired
    public TestDataInitService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public void createEntity() {

    }

}
