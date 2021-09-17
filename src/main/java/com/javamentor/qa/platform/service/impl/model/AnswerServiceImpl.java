package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private AnswerDao answerDao;
    private QuestionService questionService;

    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao, QuestionService questionService) {
        super(answerDao);
        this.answerDao = answerDao;
        this.questionService = questionService;
    }

    @Override
    @Transactional
    public Answer addAnswerOnQuestion(User user, Long questionId) {
        Optional<Question> question = questionService.getById(questionId);

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setIsDeleted(false);
        answer.setIsHelpful(false);
        answer.setHtmlBody(toString());
        answer.setQuestion(question.orElse(null));
        answer.setIsDeletedByModerator(false);
        answer.setIsDeleted(false);
        answer.setPersistDateTime(LocalDateTime.now());
        answerDao.persist(answer);
        return answer;
    }
}
