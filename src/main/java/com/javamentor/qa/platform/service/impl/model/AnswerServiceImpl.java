package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.impl.model.AnswerDaoImpl;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.model.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImpl extends ReadWriteServiceImpl<Answer, Long> implements AnswerService {

    private final AnswerDao answerDao;
    private final QuestionService questionService;


    @Autowired
    public AnswerServiceImpl(AnswerDao answerDao, QuestionService questionService) {
        super(answerDao);
        this.answerDao = answerDao;
        this.questionService = questionService;
    }

    @Override
    public Optional<Answer> getAnswerForVote(Long answerId, Long userId) {
        return answerDao.getAnswerForVote(answerId,userId);
    }
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