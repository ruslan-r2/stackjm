package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataInitService {

    @Autowired
    ReadWriteService readWriteService;

    public TestDataInitService(ReadWriteService readWriteService) {
        this.readWriteService = readWriteService;
    }

    @Transactional
    public void createEntity() {

        Role adminRole = Role.builder()
                .name("ADMIN")
                .build();

        User admin = User.builder()
                .email("admin@admin.com")
                .password("admin")
                .fullName("John Smith")
                .isEnabled(true)
                .city("CitiName")
                .linkSite("sitename.admin.com")
                .linkGitHub("admin.github.com")
                .linkVk("vk.com/admin")
                .about("It'sa me, Mario!")
                .role(adminRole)
                .nickname("IamTheLaw")
                .build();
    }

}
