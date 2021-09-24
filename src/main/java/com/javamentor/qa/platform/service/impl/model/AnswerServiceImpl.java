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
import com.javamentor.qa.platform.webapp.converters.AnswerConverter;
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
    private final AnswerConverter answerConverter;

    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao, QuestionService questionService, AnswerDtoService answerDtoService, AnswerConverter answerConverter) {
        super(answerDao);
        this.answerDao = answerDao;
        this.questionService = questionService;
        this.answerDtoService = answerDtoService;
        this.answerConverter = answerConverter;
    }

    @Override
    @Transactional
    public Answer addAnswerOnQuestion(User user, Long id, Answer answerMakeFromDto) {

        Optional<Question> question = questionService.getById(id);
        if (!question.isPresent()) {
            throw new QuestionException("Вопроса не существует");
        }

        Answer answer = new Answer();
        answer.setUser(answerMakeFromDto.getUser());
        answer.setIsDeleted(false);
        answer.setIsHelpful(false);
        answer.setQuestion(question.get());
        answer.setIsDeletedByModerator(false);
        answer.setPersistDateTime(LocalDateTime.now());
        answer.setUpdateDateTime(LocalDateTime.now());
        answer.setHtmlBody(answerMakeFromDto.getHtmlBody());
        answerDao.persist(answer);

        return answer;
    }
}

//    AnswerDto answerDto = new AnswerDto();
//        answerDto.setId(answerConverter.answerToAnswerDto(answer).getId());
//                answerDto.setQuestionId(answerConverter.answerToAnswerDto(answer).getId());
//                answerDto.setUserId(user.getId());
//                answerDto.setBody(answerConverter.answerToAnswerDto(answer).getBody());
//                answerDto.setPersistDate(answerConverter.answerToAnswerDto(answer).getPersistDate());
//                answerDto.setIsHelpful(answerConverter.answerToAnswerDto(answer).getIsHelpful());
//                answerDto.setCountUserReputation(answerConverter.answerToAnswerDto(answer).getCountUserReputation());
//                answerDao.update(answerConverter.answerDtoToAnswer(answerDto));
//                return answerConverter.answerDtoToAnswer(answerDto);
//        AnswerDto convertAnswer = answerConverter.answerToAnswerDto(answer);
//
//        Answer answer1 = new Answer();
//        answer1.setUser(user);
//        answer1.setIsDeleted(false);
//        answer1.setIsHelpful(false);
//        answer1.setQuestion(question.get());
//        answer1.setIsDeletedByModerator(false);
//        answer1.setPersistDateTime(LocalDateTime.now());
//        answer1.setUpdateDateTime(LocalDateTime.now());
//        answer1.setHtmlBody(convertAnswer.getBody());
//        answerDao.persist(answer1);