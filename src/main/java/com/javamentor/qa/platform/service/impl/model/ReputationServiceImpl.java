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

    private final ReputationDao reputationDao;


    public ReputationServiceImpl(ReadWriteDao<Reputation, Long> readWriteDao, AnswerService answerService, ReputationDao reputationDao) {
        super(readWriteDao);
        this.answerService = answerService;
        this.reputationDao = reputationDao;
    }

    @Override
//    @Transactional
    public void changeRep(Long answerId, User user, Integer count) {
        Optional<Answer> optionalAnswer = answerService.getById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            Reputation reputation = new Reputation();
            reputation.setAnswer(answer);
            reputation.setAuthor(answer.getUser());
            reputation.setCount(count);
            reputation.setPersistDate(LocalDateTime.now());
            reputation.setSender(user);
            reputation.setType(ReputationType.Answer);
            try {
                reputationDao.persist(reputation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Integer getRepByUserId(Long id) {
        return reputationDao.getRepByUserId(id);
    }

}
