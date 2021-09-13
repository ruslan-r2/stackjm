package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.RollbackException;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private AnswerDao answerDao;
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;
    private AnswerConverter answerConverter;


    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao) {
        super(answerDao);
        this.answerDao = answerDao;
    }


    @Override
    @Transactional
    public Answer addAnswer(Answer answer, User user, Question question, Long id) {
        Answer answerForQuestion = answerDao.getById(id).get();

        answerForQuestion.setUser(user);
        answerForQuestion.setQuestion(question);

        answerDao.persist(answerForQuestion);

        return answerForQuestion;
    }
}
