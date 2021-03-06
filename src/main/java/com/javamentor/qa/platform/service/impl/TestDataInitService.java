package com.javamentor.qa.platform.service.impl;


import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.VoteTypeQ;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteType;
import com.javamentor.qa.platform.models.entity.user.Role;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.*;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class    TestDataInitService {
    private UserService userService;
    private RoleService roleService;
    private QuestionService questionService;
    private AnswerService answerService;
    private VoteAnswerService voteAnswerService;
    private VoteQuestionService voteQuestionService;
    private final Flyway flyway;

    @Autowired
    public TestDataInitService(UserService userService, RoleService roleService, QuestionService questionService,
                               AnswerService answerService, VoteAnswerService voteAnswerService,
                               VoteQuestionService voteQuestionService, Flyway flyway) {
        this.userService = userService;
        this.roleService = roleService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.voteAnswerService = voteAnswerService;
        this.voteQuestionService = voteQuestionService;
        this.flyway = flyway;
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public void createEntity() {
        flyway.clean();
        flyway.migrate();
        createUsers();
        createQuestions();
        createAnswers();
        createVotes();

    }

    private User admin;
    private User user;

    private void createUsers() {
        Role adminRole = new Role("ROLE_ADMIN");
        roleService.persist(adminRole);
        Role userRole = new Role ("ROLE_USER");
        roleService.persist(userRole);

        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword(passwordEncoder().encode("admin"));
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
        this.admin = admin;
        userService.persist(admin);

        for (int i = 1; i < 5; i++) {
        User user = new User();
        user.setEmail("user" + i + "@user.com");
        user.setPassword(passwordEncoder().encode("user" + i));
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
        this.user = user;
        userService.persist(user);
        }
    }

    private List<Tag> tags = new ArrayList<>();
    private List<Tag> tags2 = new ArrayList<>();

    private void createTags() {
        tags = new ArrayList<>();
        int tagCount = new Random().nextInt(4) + 1;
        for (int i = 0; i < tagCount; i++) {
            Tag tag = new Tag();
            tag.setName("tagName" + i);
            tag.setDescription("tagDescription" + i);
            tags.add(tag);
        }
    }

    private List<Question> questions = new ArrayList<>();

    private void createQuestions() {
        for (int i = 0; i < 40; i++) {
            createTags();
            Question question = new Question();
            question.setUser(user);
            question.setTags(tags);
            question.setTitle("questionTitle" + i);
            question.setIsDeleted(new Random().nextBoolean());
            question.setDescription("questionDescription" + i);
            question.setAnswers(answers);
            questions.add(question);
            questionService.persist(question);
        }
    }

    private List<Answer> answers = new ArrayList<>();

    private void createAnswers() {
        for (int i = 0; i < 40; i++) {
            Answer answer = new Answer();
            answer.setQuestion(questions.get(i));
            answer.setIsHelpful(new Random().nextBoolean());
            answer.setIsDeletedByModerator(new Random().nextBoolean());
            answer.setIsDeleted(new Random().nextBoolean());
            answer.setHtmlBody("htmlBody" + i);
            answer.setUser(user);
            answers.add(answer);
            answerService.persist(answer);
        }
    }

    private List<VoteQuestion> votesQuestion = new ArrayList<>();
    private List<VoteAnswer> votesAnswer = new ArrayList<>();

    private void createVotes() {
        for (int i = 0; i < 30; i++) {
            VoteQuestion voteQuestion = new VoteQuestion();
            voteQuestion.setQuestion(questions.get(i % questions.size()));
            voteQuestion.setVoteTypeQ(new Random().nextBoolean() ? VoteTypeQ.UP : VoteTypeQ.DOWN);
            voteQuestion.setUser(user);
            votesQuestion.add(voteQuestion);
            voteQuestionService.persist(voteQuestion);
        }

        for (int i = 0; i < 30; i++) {
            VoteAnswer voteAnswer = new VoteAnswer(
                    user,
                    answers.get(i % answers.size()),
                    new Random().nextBoolean() ? VoteType.UP : VoteType.DOWN
            );
            votesAnswer.add(voteAnswer);
            voteAnswerService.persist(voteAnswer);
        }
    }
}
