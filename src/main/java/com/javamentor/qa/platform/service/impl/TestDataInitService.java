package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.ReadWriteService;
import com.javamentor.qa.platform.service.impl.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestDataInitService {

    private UserServiceImpl userService;
    private RoleServiceImpl roleService;
    private QuestionServiceImpl questionService;
    private AnswerServiceImpl answerService;
    private TagServiceImpl tagService;

    @Autowired
    public TestDataInitService(UserServiceImpl userService,
                               RoleServiceImpl roleService,
                               QuestionServiceImpl questionService,
                               AnswerServiceImpl answerService,
                               TagServiceImpl tagService) {
        this.userService = userService;
        this.roleService = roleService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.tagService = tagService;
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
