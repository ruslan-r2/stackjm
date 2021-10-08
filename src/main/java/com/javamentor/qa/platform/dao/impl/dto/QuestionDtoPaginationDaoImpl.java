package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
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
        List<Long> tagIdList = null;
        List<Long> trackedTagList = (List<Long>) parameters.get("trackedTag");
        List<Long> ignoredTagList = (List<Long>) parameters.get("ignoredTag");
        StringBuilder queryBuilder = new StringBuilder();

        if (trackedTagList != null) {
            if (trackedTagList.isEmpty()) {
                return Collections.emptyList();
            }
            tagIdList = trackedTagList;
            queryBuilder.append("and q.id in (select tq.id from Tag t join t.questions tq where t.id in :list) ");
            if (ignoredTagList != null) {
                if (!ignoredTagList.isEmpty()) {
                    tagIdList.removeAll(ignoredTagList);
                }
                if (tagIdList.isEmpty()) {
                    return Collections.emptyList();
                }
            }
        } else if (ignoredTagList != null && !ignoredTagList.isEmpty()) {
            tagIdList = ignoredTagList;
            queryBuilder.append("and q.id in (select tq.id from Tag t join t.questions tq where t.id not in :list) ");
        }

        Query query = entityManager.createQuery("select q.id as id," +
                " q.title as title," +
                " q.user.id as authorId," +
                " q.user.fullName as authorName," +
                " q.user.imageLink as authorImage," +
                " q.description as description, " +
                "(select coalesce(count(*), 0)  from QuestionViewed  where question.id = q.id) as viewCount, " +
                "(select coalesce(sum(r.count),0) from Reputation r where r.author.id = q.user.id) as authorReputation, " +
                "(select count(*) from Answer a where a.question.id = q.id) as countAnswer, " +
                "(select coalesce(sum(v.vote),0) from VoteQuestion v where v.question.id = q.id) as countValuable, " +
                "q.persistDateTime as persistDateTime," +
                "q.lastUpdateDateTime as lastUpdateDateTime " +
                "from Question q " +
                "where q.isDeleted = false " + queryBuilder +
                "order by q.id");
        if (tagIdList != null && !tagIdList.isEmpty()) {
            query.setParameter("list", tagIdList);
        }
        query.unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(QuestionDto.class))
                .setFirstResult((Integer) parameters.get("itemsOnPage") * ((Integer) parameters.get("currentPage") - 1))
                .setMaxResults((Integer) parameters.get("itemsOnPage"));
        List<QuestionDto> result = query.getResultList();
        return result;
    }
}


