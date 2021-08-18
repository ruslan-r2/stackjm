package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestDataInitService {

    private final UserService userService;
    private final RoleService roleService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final TagService tagService;

    @Autowired
    public TestDataInitService(UserService userService,
                               RoleService roleService,
                               QuestionService questionService,
                               AnswerService answerService,
                               TagService tagService) {
        this.userService = userService;
        this.roleService = roleService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.tagService = tagService;
    }

    @Transactional
    public void createEntity() {
        createAnswer();
    }

    @Transactional
    public User createUser() {
        User user = new User();
        user.setEmail("maza120@yandex.ru");
        user.setPassword("123");
        user.setFullName("Zalyalov Almaz Fayazovich");
        user.setIsEnabled(true);
        user.setCity("Kazan");
        user.setLinkSite("domen.site.com");
        user.setLinkGitHub("almaz.github.com");
        user.setLinkVk("vk.com/almaz");
        user.setImageLink("images.com/link");
        user.setAbout("I love Java");
        user.setRole(createRole());
        user.setNickname("Maza");
        userService.persist(user);
        return user;
    }

    @Transactional
    public Role createRole() {
        Role role = new Role();
        role.setName("USER");
        roleService.persist(role);
        return role;
    }

    @Transactional
    public Question createQuestion(int count) {
        Question question = new Question();
        question.setTitle("title" + count);
        question.setDescription("description" + count);
        question.setUser(createUser());
        question.setIsDeleted(false);
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tag tag = createTag(i);
            tags.add(tag);
            question.setTags(tags);
            questionService.persist(question);
        }
        return question;
    }

    @Transactional
    public void createAnswer() {
        Answer answer;
        for (int i = 0; i < 40; i++) {
            answer = new Answer();
            answer.setHtmlBody("htmlBody" + i);
            answer.setIsHelpful(true);
            answer.setIsDeleted(false);
            answer.setIsDeletedByModerator(false);
            answer.setQuestion(createQuestion(i));
            answer.setUser(createUser());
            answerService.persist(answer);
        }
    }

    @Transactional
    public Tag createTag(int count) {
        Tag tag = new Tag();
        tag.setName("tagName" + count);
        tag.setDescription("tagDescription" + count);
        tagService.persist(tag);
        return tag;
    }
}
