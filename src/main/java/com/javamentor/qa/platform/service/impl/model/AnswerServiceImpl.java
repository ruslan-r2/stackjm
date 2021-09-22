package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.exception.QuestionException;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public Answer addAnswerOnQuestion(User user, Long id, AnswerDto answerDto) {

        Optional<Question> question = questionService.getById(id);

        if (!question.isPresent()) {
            throw new QuestionException("Вопроса не существует");
        }

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setIsDeleted(false);
        answer.setIsHelpful(false);
        answer.setQuestion(question.get());
        answer.setIsDeletedByModerator(false);
        answer.setPersistDateTime(LocalDateTime.now());
//        answer.setDateAcceptTime(LocalDateTime.now());
        answer.setUpdateDateTime(LocalDateTime.now());
        answer.setHtmlBody(answerDto.getBody());
        answerDao.update(answer);
//        answer.setHtmlBody(answerDtoService.getAnswerDtoById(id).getBody());
//        answerDao.update(answer);
        return answer;
    }
}