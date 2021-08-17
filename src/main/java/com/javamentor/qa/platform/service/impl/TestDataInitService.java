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
        createUser();
        createQuestion();
        createAnswer();
        createTag();
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
        role.setName("ADMIN");
        roleService.persist(role);
        return role;
    }

    @Transactional
    public Question createQuestion() {
        Question question = new Question();
        question.setTitle("title");
        question.setDescription("description");
        question.setUser(createUser());
        question.setIsDeleted(false);
        List<Tag> tags = new ArrayList<>();
        tags.add(createTag());
        question.setTags(tags);
        questionService.persist(question);
        return question;
    }

    @Transactional
    public void createAnswer() {
        Answer answer = new Answer();
        answer.setUser(createUser());
        answer.setHtmlBody("htmlBody");
        answer.setIsHelpful(true);
        answer.setIsDeleted(false);
        answer.setIsDeletedByModerator(false);
        answer.setQuestion(createQuestion());
        answerService.persist(answer);
    }

    @Transactional
    public Tag createTag() {
        Tag tag = new Tag();
        tag.setName("tagName");
        tag.setDescription("tagDescription");
        tagService.persist(tag);
        return tag;
    }
}
