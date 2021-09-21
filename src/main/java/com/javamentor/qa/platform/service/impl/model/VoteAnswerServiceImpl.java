package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.*;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private final AnswerDao answerDao;
    private final VoteAnswerDao voteAnswerDao;
    private final ReputationDao reputationDao;

    public VoteAnswerServiceImpl(ReadWriteDao<VoteAnswer, Long> readWriteDao, AnswerDao answerDao, VoteAnswerDao voteAnswerDao, ReputationDao reputationDao) {
        super(readWriteDao);
        this.answerDao = answerDao;
        this.voteAnswerDao = voteAnswerDao;
        this.reputationDao = reputationDao;
    }

    @Override
    public Long voteUp(Long answerId, User user) {
        return vote(answerId, user, 10);
    }

    @Override
    public Long voteDown(Long answerId, User user) {
        return vote(answerId, user, -5);
    }

    private Long vote(Long answerId, User user, int count) {
        Optional<Answer> answerOptional = answerDao.getById(answerId);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            VoteAnswer voteAnswer = new VoteAnswer();
            voteAnswer.setVote(1);
            voteAnswer.setAnswer(answer);
            voteAnswer.setUser(user);
            voteAnswer.setPersistDateTime(LocalDateTime.now());
            Reputation reputation = new Reputation();
            reputation.setAnswer(answer);
            reputation.setAuthor(answer.getUser());
            reputation.setCount(count);
            reputation.setSender(user);
            reputation.setType(ReputationType.Answer);
            try {
                voteAnswerDao.persist(voteAnswer);
                reputationDao.persist(reputation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voteAnswerDao.sumVote(answerId);
    }
}
