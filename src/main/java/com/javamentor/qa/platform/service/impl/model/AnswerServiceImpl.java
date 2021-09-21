package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.exception.QuestionException;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private final AnswerDao answerDao;
    private final QuestionService questionService;
    private final AnswerDtoService answerDtoService;

    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao, QuestionService questionService, AnswerDtoService answerDtoService) {
        super(answerDao);
        this.answerDao = answerDao;
        this.questionService = questionService;
        this.answerDtoService = answerDtoService;
    }

    @Override
    @Transactional
    public Answer addAnswerOnQuestion(User user, Long id) {

        if (!questionService.getById(id).isPresent()) {
            throw new QuestionException("Вопроса не существует");
        }

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setIsDeleted(false);
        answer.setIsHelpful(false);
//        answer.setHtmlBody(answerDtoService.getAnswerById(id).getBody());
        answer.setQuestion(questionService.getById(id).get());
        answer.setIsDeletedByModerator(false);
        answer.setIsDeleted(false);
        answer.setPersistDateTime(LocalDateTime.now());
        answerDao.persist(answer);
        answer.setHtmlBody(answerDtoService.getAnswerById(answer.getId()).getBody());
        answerDao.update(answer);
        return answer;
    }

    /*"(select COALESCE(SUM(vote), 0)  from VoteAnswer  where answer.id = a.id) as countValuable, " +
                "(select COALESCE(SUM(r.count), 0)  from Reputation r  where author.id = a.user.id) as countUserReputation "*/
}
