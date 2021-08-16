package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.dto.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TestDataInitService {


    private UserService userService;

    @Autowired
    public TestDataInitService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public void createEntity() {
        Role adminRole = new Role ("ADMIN");

        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword("admin");
        admin.setFullName("John Smith");
        admin.setPersistDateTime(LocalDateTime.now());
        admin.setIsEnabled(true);
        admin.setCity("CitiName");
        admin.setImageLink("image.link.com");
        admin.setLinkSite("sitename.admin.com");
        admin.setLinkGitHub("admin.github.com");
        admin.setLinkVk("vk.com/admin");
        admin.setAbout("It'sa me, Mario!");
        admin.setNickname("IamTheLaw");
        admin.setLastUpdateDateTime(LocalDateTime.now());
        admin.setRole(adminRole);
        userService.persist(admin);
    }

}
