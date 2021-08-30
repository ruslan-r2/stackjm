package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.exception.ApiRequestException;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


@Repository
public class AnswerDtoDaoImpl implements AnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnswerDto> getAllAnswersByQuestionId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("SELECT t1.id as id, t1.user.id as userId, t1.question.id as questionId," +
                " t1.htmlBody as body, t1.persistDateTime as persistDate, t1.isHelpful as isHelpful," +
                " t1.dateAcceptTime as dateAccept, " +
                "t1.user.imageLink as image, t1.user.nickname as nickname " +
                "FROM Answer t1 " +
                "WHERE t1.question.id = " + id
        )
                .setResultTransformer(new AliasToBeanResultTransformer(AnswerDto.class));

        List<AnswerDto> answerDtoList = query.getResultList();



        for (AnswerDto answerDto : answerDtoList){
            try {
                List a = entityManager.createQuery("select sum(t1.vote) from VoteAnswer t1 where t1.answer.id = " + answerDto.getId()).getResultList();
                answerDto.setCountValuable(Long.parseLong(a.get(0).toString()));
            } catch (NullPointerException e){
                answerDto.setCountValuable(0L);
            }
        }
        return answerDtoList;


    }
}
