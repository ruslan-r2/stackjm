package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

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
                "(select coalesce(sum(case when va.voteType = 'UP' then 1 when va.voteType = 'DOWN' then -1 end), 0) " +
                "from VoteAnswer va where answer.id = a.id) as countValuable, " +
                "(select count(va.id) from VoteAnswer va where answer.id = a.id) as countVote, " +
                "(select coalesce(va.voteType, null) " +
                "from VoteAnswer va where answer.id = a.id and user.id = a.user.id ) as voteType, " +
                "(select coalesce(sum(r.count), 0) from Reputation r where author.id = a.user.id) as countUserReputation " +
                "from Answer a " +
                "where a.question.id = :id and a.isDeleted = false " +
                "group by a.id, a.user.id, a.question.id, a.htmlBody, a.persistDateTime, a.isHelpful, " +
                "a.dateAcceptTime, a.user.imageLink, a.user.nickname")
                .setParameter("id", id)
                .setResultTransformer(new AliasToBeanResultTransformer(AnswerDto.class));
        return query.getResultList();
    }

    @Override
    public Optional<AnswerDto> getAnswerDtoById(Long id) {
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
                "(select coalesce(sum(case when va.voteType = 'UP' then 1 when va.voteType = 'DOWN' then -1 end), 0) " +
                "from VoteAnswer va where answer.id = a.id) as countValuable, " +
                "(select count(va.id) from VoteAnswer va where answer.id = a.id) as countVote, " +
                "(select coalesce(va.voteType, null) " +
                "from VoteAnswer va where answer.id = a.id and user.id = a.user.id ) as voteType, " +
                "(select coalesce(sum(r.count), 0) from Reputation r where author.id = a.user.id) as countUserReputation " +
                "from Answer a " +
                "where a.id = :id and a.isDeleted = false")
                .setParameter("id", id)
                .setResultTransformer(new AliasToBeanResultTransformer(AnswerDto.class));
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}