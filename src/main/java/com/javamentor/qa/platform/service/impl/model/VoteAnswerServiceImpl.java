package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.*;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.question.answer.VoteAnswer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteAnswerServiceImpl extends ReadWriteServiceImpl<VoteAnswer, Long> implements VoteAnswerService {

    private final AnswerService answerService;
    private final VoteAnswerDao voteAnswerDao;
    private final ReputationService reputationService;

    public VoteAnswerServiceImpl(ReadWriteDao<VoteAnswer, Long> readWriteDao, AnswerService answerService, VoteAnswerDao voteAnswerDao, ReputationService reputationService) {
        super(readWriteDao);
        this.answerService = answerService;
        this.voteAnswerDao = voteAnswerDao;
        this.reputationService = reputationService;
    }

    @Override
    public Long voteUp(Long answerId, User user) {
        return vote(answerId, user, 10,1);
    }

    @Override
    public Long voteDown(Long answerId, User user) {
        return vote(answerId, user, -5,-1);
    }
//    @Transactional
    private Long vote(Long answerId, User user, int count, int vote) {
        Optional<Answer> answerOptional = answerService.getById(answerId);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            VoteAnswer voteAnswer = new VoteAnswer();
            voteAnswer.setVote(vote);
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
                voteAnswerDao.update(voteAnswer);
                reputationService.update(reputation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voteAnswerDao.sumVote(answerId);
    }
}
