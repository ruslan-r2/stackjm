package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.VoteQuestionDao;
import com.javamentor.qa.platform.exception.VoteException;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.VoteQuestion;
import com.javamentor.qa.platform.models.entity.question.VoteTypeQ;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.models.entity.user.reputation.Reputation;
import com.javamentor.qa.platform.models.entity.user.reputation.ReputationType;
import com.javamentor.qa.platform.service.abstracts.model.ReputationService;
import com.javamentor.qa.platform.service.abstracts.model.VoteQuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class  VoteQuestionServiceImpl extends ReadWriteServiceImpl<VoteQuestion, Long> implements VoteQuestionService {

    private VoteQuestionDao voteQuestionDao;
    private ReputationService reputationService;

    public VoteQuestionServiceImpl(VoteQuestionDao voteQuestionDao, ReputationService reputationService) {
        super(voteQuestionDao);
        this.voteQuestionDao = voteQuestionDao;
        this.reputationService = reputationService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VoteQuestion> getByUserAndQuestion(User user, Question question) {
        return voteQuestionDao.getByUserAndQuestion(user, question);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getSumVoteForQuestion(Question question) {
        return voteQuestionDao.getSumVoteForQuestion(question);
    }

    @Override
    @Transactional
    public void upVote(User user, Question question) throws VoteException {
        if (question.getUser().equals(user)) {
            throw new VoteException("пользователь не может голосовать за свой вопрос");
        }

        Optional<VoteQuestion> optionalVoteQuestion = getByUserAndQuestion(user, question);
        if (optionalVoteQuestion.isPresent()) {
            if(optionalVoteQuestion.get().getVoteTypeQ().equals(VoteTypeQ.UP)) {
                throw new VoteException("пользователь проголосовал 'за' ранее");
            } else {
                optionalVoteQuestion.get().setVoteTypeQ(VoteTypeQ.UP);
                update(optionalVoteQuestion.get());
            }
        } else {
            persist(new VoteQuestion(user, question, VoteTypeQ.UP));
        }

        Optional<Reputation> optionalReputation = reputationService.getByAuthorAndSenderAndQuestionAndType(question.getUser(), user, question, ReputationType.VoteQuestion);
        if (optionalReputation.isPresent()) {
            optionalReputation.get().setCount(10);
            reputationService.update(optionalReputation.get());
        } else {
            Reputation reputationAuthor = new Reputation();
            reputationAuthor.setPersistDate(LocalDateTime.now());
            reputationAuthor.setAuthor(question.getUser());
            reputationAuthor.setSender(user);
            reputationAuthor.setCount(10);
            reputationAuthor.setType(ReputationType.VoteQuestion);
            reputationAuthor.setQuestion(question);
            reputationService.persist(reputationAuthor);
        }
    }

    @Override
    @Transactional
    public void downVote(User user, Question question) throws VoteException {
        if (question.getUser().equals(user)) {
            throw new VoteException("пользователь не может голосовать за свой вопрос");
        }

        Optional<VoteQuestion> optionalVoteQuestion = getByUserAndQuestion(user, question);
        if (optionalVoteQuestion.isPresent()) {
            if(optionalVoteQuestion.get().getVoteTypeQ().equals(VoteTypeQ.DOWN)) {
                throw new VoteException("пользователь проголосовал 'против' ранее");
            } else {
                optionalVoteQuestion.get().setVoteTypeQ(VoteTypeQ.DOWN);
                update(optionalVoteQuestion.get());
            }
        } else {
            persist(new VoteQuestion(user, question, VoteTypeQ.DOWN));
        }

        Optional<Reputation> optionalReputation = reputationService.getByAuthorAndSenderAndQuestionAndType(question.getUser(), user, question, ReputationType.VoteQuestion);
        if (optionalReputation.isPresent()) {
            optionalReputation.get().setCount(-5);
            reputationService.update(optionalReputation.get());
        } else {
            Reputation reputationAuthor = new Reputation();
            reputationAuthor.setPersistDate(LocalDateTime.now());
            reputationAuthor.setAuthor(question.getUser());
            reputationAuthor.setSender(user);
            reputationAuthor.setCount(-5);
            reputationAuthor.setType(ReputationType.VoteQuestion);
            reputationAuthor.setQuestion(question);
            reputationService.persist(reputationAuthor);
        }
    }
}
