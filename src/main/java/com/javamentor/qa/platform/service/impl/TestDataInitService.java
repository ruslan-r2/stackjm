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
    public void createEntity(User user, Role role, Question question, Answer answer, Tag tag) {
        userService.persist(user);
        roleService.persist(role);
        questionService.persist(question);
        answerService.persist(answer);
        tagService.persist(tag);
    }
}
