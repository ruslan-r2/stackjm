package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.ReadWriteDao;
import com.javamentor.qa.platform.dao.abstracts.model.ReputationDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReputationServiceImpl extends ReadWriteServiceImpl<Reputation, Long> implements ReputationService {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final ReputationDao reputationDao;


    public ReputationServiceImpl(ReadWriteDao<Reputation, Long> readWriteDao, AnswerService answerService, QuestionService questionService, ReputationDao reputationDao) {
        super(readWriteDao);
        this.answerService = answerService;
        this.questionService = questionService;
        this.reputationDao = reputationDao;
    }

    @Override
    @Transactional
    public void changeRep(Long questId, Long answerId, User user, Integer count) {
        Answer answer = getAnswer(answerId).get();
        Question question = getQuestion(questId).get();
        Reputation reputation = new Reputation();
        reputation.setAnswer(answer);
        reputation.setAuthor(answer.getUser());
        reputation.setCount(count);
        reputation.setQuestion(question);
        reputation.setPersistDate(LocalDateTime.now());
        reputation.setSender(user);
        reputation.setType(ReputationType.Answer);
        try {
            reputationDao.persist(reputation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getRepByUserId(Long id) {
        return reputationDao.getRepByUserId(id);
    }

    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public Optional<Answer> getAnswer(Long id) {
        return answerService.getById(id);
    }
    @Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
    public Optional<Question> getQuestion(Long id) {
        return questionService.getById(id);
    }
}
