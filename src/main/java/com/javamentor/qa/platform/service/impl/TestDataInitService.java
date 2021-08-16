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

@Service
public class TestDataInitService {

    private UserService userService;
    private RoleService roleService;
    private QuestionService questionService;
    private AnswerService answerService;
    private TagService tagService;

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

    }

    @Transactional
    public void createUser(User user) {
        userService.persist(user);
    }

    @Transactional
    public void createRole(Role role) {
        roleService.persist(role);
    }

    @Transactional
    public void createQuestion(Question question) {
        questionService.persist(question);
    }

    @Transactional
    public void createAnswer(Answer answer) {
        answerService.persist(answer);
    }

    @Transactional
    public void createTag(Tag tag) {
        tagService.persist(tag);
    }
}
