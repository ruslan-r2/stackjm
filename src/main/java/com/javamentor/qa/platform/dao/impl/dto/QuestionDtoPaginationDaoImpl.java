package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("allQuestions")
public class QuestionDtoPaginationDaoImpl implements PaginationDao<QuestionDto> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountOfAllItems(Map<String, Object> parameters) {
        Long count = entityManager
                .createQuery("SELECT count(question) FROM Question question WHERE question.isDeleted = false", Long.class)
                .getSingleResult();
        return count.intValue();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<QuestionDto> getItems(Map<String, Object> parameters) {
        String noAnswers = "";

        if (parameters.containsKey("noAnswers") && parameters.get("noAnswers").equals(true)) {
            noAnswers = "and (select count(*) from q.answers qa where qa.isDeleted = false) = 0 ";
        }

        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select q.id as id," +
                " q.title as title," +
                " q.user.id as authorId," +
                " q.user.fullName as authorName," +
                " q.user.imageLink as authorImage," +
                " q.description as description, " +
                "(select coalesce(count(*), 0)  from QuestionViewed  where question.id = q.id) as viewCount, " +
                "(select coalesce(sum(r.count),0) from Reputation r where r.author.id = q.user.id) as authorReputation, " +
                "(select count(*) from Answer a where a.question.id = q.id and a.isDeleted = false) as countAnswer, " +
                "(select coalesce(sum(case when v.voteTypeQ = 'UP' then 1 when v.voteTypeQ = 'DOWN' then -1 end), 0) from VoteQuestion v where v.question.id = q.id) as countValuable, " +
                "q.persistDateTime as persistDateTime," +
                "q.lastUpdateDateTime as lastUpdateDateTime," +
                "(select count(*) from VoteQuestion vq where vq.question.id = q.id) as countVote," +
                "(select vq.voteTypeQ from VoteQuestion vq where vq.user.id = :authorizedUserId and vq.question.id = q.id) as voteType " +
                "from Question q " +
                "where q.isDeleted = false " +
                "and (coalesce(:trackedTags, null) is null or (q.id in (select tq.id from Tag t join t.questions tq where t.id in (:trackedTags)))) " +
                "and (coalesce(:ignoredTags, null) is null or (q.id not in (select tq.id from Tag t join t.questions tq where t.id in (:ignoredTags)))) " +
                noAnswers +
                "order by q.id");

        query.setParameter("authorizedUserId", parameters.get("authorizedUserId"));
        query.setParameter("trackedTags", parameters.get("trackedTag"));
        query.setParameter("ignoredTags", parameters.get("ignoredTag"));

        query.setResultTransformer(new AliasToBeanResultTransformer(QuestionDto.class))
                .setFirstResult((Integer) parameters.get("itemsOnPage") * ((Integer) parameters.get("currentPage") - 1))
                .setMaxResults((Integer) parameters.get("itemsOnPage"));
        return query.getResultList();
    }
}
