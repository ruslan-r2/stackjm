package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.impl.dto.RoleService;
import com.javamentor.qa.platform.service.impl.dto.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TestDataInitService {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public TestDataInitService(UserService userService, RoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Transactional
    public void createEntity() {
        createUserEntity();
    }

    private void createUserEntity() {
        Role adminRole = new Role("ROLE_ADMIN");
        roleService.persist(adminRole);
        Role userRole = new Role ("ROLE_USER");
        roleService.persist(userRole);

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

        for (int i = 1; i < 41; i++) {
        User user = new User();
        user.setEmail("user" + i + "@user.com");
        user.setPassword( "user" + i);
        user.setFullName(" User user" +i+ "");
        user.setPersistDateTime(LocalDateTime.now());
        user.setIsEnabled(true);
        user.setCity("CitiName" + i );
        user.setImageLink("user" + i + ".image.link.com");
        user.setLinkSite("sitename.user" + i + ".com");
        user.setLinkGitHub("user" + i + ".github.com");
        user.setLinkVk("vk.com/user" + i );
        user.setAbout("I am User" + i);
        user.setNickname("user" + i + "NickName");
        user.setLastUpdateDateTime(LocalDateTime.now());
        user.setRole(userRole);
        userService.persist(user);
        }

    }
}
