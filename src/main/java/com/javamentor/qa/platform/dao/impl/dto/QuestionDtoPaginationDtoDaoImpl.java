package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository("allQuestions")
public class QuestionDtoPaginationDtoDaoImpl implements PaginationDao<QuestionDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountOfAllItems(Map<String, Object> parameters) {
        Long count = entityManager
                .createQuery("SELECT count(q) FROM Question q WHERE q.isDeleted = false", Long.class)
                .getSingleResult();
        return count.intValue();
    }

    @Override
    @SuppressWarnings({"deprecation", "unchecked"})
    public List<QuestionDto> getItems(Map<String, Object> parameters) {
        int itemsOnPage = (int) parameters.get("itemsOnPage");
        int currentPage = (int) parameters.get("currentPage");

        String sorted = "ORDER BY";

        if (parameters.containsKey("sorted-reputation") && parameters.get("sorted-reputation").equals(true)){
            sorted += " reputation DESC, ";
        }
        if (parameters.containsKey("sorted-registrationDate") && parameters.get("sorted-registrationDate").equals(true)){
            sorted += " registrationDate DESC, ";
        }

        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select " +
                        "q.id as id, " +
                        "q.title as title, " +
                        "q.user.id as authorId, " +
                        "q.user.fullName as authorName, " +
                        "q.user.imageLink as authorImage,  " +
                        "q.description as description, " +
                        "q.persistDateTime as persistDateTime, " +
                        "q.lastUpdateDateTime as lastUpdateDateTime, " +
//                        "q.tags as listTagDto, " +
                        "(select COALESCE(count(*), 0) from QuestionViewed where question.id = q.id) as viewCount," +
                        "(select COALESCE(count(*), 0) from Answer a where question.id = q.id)   as countAnswer, " +
                        "(select COALESCE(SUM(vote), 0) from VoteQuestion where question.id = q.id) as countValuable, " +
                        "(select COALESCE(SUM(r.count), 0) from Reputation r where author.id = q.user.id) as authorReputation " +
                        "from Question q " +
                        "where q.isDeleted = false " +
                        "group by q.id, q.title, q.user.id, q.user.fullName, q.user.imageLink, q.description, q.persistDateTime, q.lastUpdateDateTime")
                .setResultTransformer(new AliasToBeanResultTransformer(QuestionDto.class))
                .setFirstResult(itemsOnPage * (currentPage - 1))
                .setMaxResults(itemsOnPage);
        return query.getResultList();
    }
}
