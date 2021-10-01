package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDto> getAllAnswersByQuestionId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select a.id as id, " +
                "a.user.id as userId," +
                "a.question.id as questionId," +
                "a.htmlBody as body, " +
                "a.persistDateTime as persistDate, " +
                "a.isHelpful as isHelpful, " +
                "a.dateAcceptTime as dateAccept, " +
                "a.user.imageLink as image, " +
                "a.user.nickname as nickname, " +
                "(select COALESCE(SUM(vote), 0)  from VoteAnswer  where answer.id = a.id) as countValuable, " +
                "(select COALESCE(SUM(r.count), 0)  from Reputation r  where author.id = a.user.id) as countUserReputation " +
                "from Answer a " +
                "where a.question.id = :id and a.isDeleted = false " +
                "group by a.id, a.user.id, a.question.id,a.htmlBody, a.persistDateTime, a.isHelpful, a.dateAcceptTime, a.user.imageLink, a.user.nickname")
                .setParameter("id",id)
                .setResultTransformer(new AliasToBeanResultTransformer(AnswerDto.class));
        List<AnswerDto> answerDtoList = query.getResultList();
        return answerDtoList;
    }

    @Override
    public AnswerDto getAnswerDtoById(Long id) throws NoResultException {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("SELECT a.id as id, " +
                        "a.user.id as userId," +
                        "a.question.id as questionId," +
                        "a.htmlBody as body, " +
                        "a.persistDateTime as persistDate, " +
                        "a.isHelpful as isHelpful, " +
                        "a.dateAcceptTime as dateAccept, " +
                        "a.user.imageLink as image, " +
                        "a.user.nickname as nickname, " +
                        "(select COALESCE(SUM(vote), 0)  from VoteAnswer  where answer.id = a.id) as countValuable, " +
                        "(select COALESCE(SUM(r.count), 0)  from Reputation r  where author.id = a.user.id) as countUserReputation " +
                        "from Answer a " +
                        "where a.id = :id and a.isDeleted = false ")
                .setParameter("id", id)
                .setResultTransformer(new AliasToBeanResultTransformer(AnswerDto.class));
        return (AnswerDto)query.getSingleResult();
    }

    @Override
    @Transactional
    public void updateAnswer(Long answerId, AnswerDto answerDto) throws RuntimeException{

        if(answerDto.getBody().isEmpty() || answerDto.getBody().length() == 0) {
            throw new RuntimeException();
        }

        entityManager.createQuery("UPDATE Answer a SET a.htmlBody = :body, a.updateDateTime = :time WHERE a.id = :id ")
                .setParameter("id", answerId)
                .setParameter("body", answerDto.getBody())
                .setParameter("time", LocalDateTime.now())
                .executeUpdate();
    }
}