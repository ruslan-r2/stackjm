package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagDto> getByQuestionId(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("select t.id as id, " +
                        "t.name as name, " +
                        "t.description as description " +
                        "from Question q " +
                        "join q.tags t " +
                        "where q.id = :id")
                .setParameter("id", id).setResultTransformer(new AliasToBeanResultTransformer(TagDto.class));
        return query.getResultList();
    }

    @SuppressWarnings("deprecation")
    public Map<Long, List<TagDto>> getTagsByQuestionIdList(List<Long> questionIdList) {
        Query query = entityManager.createQuery("select q.id as questionId, " +
                "t.id as tagIid, " +
                "t.name as tagName, " +
                "t.description as tagDescription " +
                "from Tag t " +
                "join t.questions q " +
                "where q.id in :list").setParameter("list", questionIdList)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionIdTagDtoMapResultTransformer());
        Map resultMap = (Map<Long, List<TagDto>>) query.getResultList().get(0);
        return resultMap;
    }

    @SuppressWarnings("unchecked")
    public List<RelatedTagDto> getTopTags() {
        return (List<RelatedTagDto>) entityManager.createQuery(
                        "SELECT tag.id AS id, " +
                                "tag.name AS title, " +
                                "CAST (tag.questions.size AS long) AS countQuestion " +
                                "FROM Tag tag ORDER BY tag.questions.size DESC").unwrap(Query.class)
                .setResultTransformer(Transformers.aliasToBean(RelatedTagDto.class))
                .setMaxResults(10)
                .getResultList();
    }

    public List<IgnoredTagDto> getIgnoredTagsByUserId(Long id) {
        return entityManager
                .createQuery(
                        "SELECT NEW com.javamentor.qa.platform.models.dto.IgnoredTagDto(t.id, t.name)" +
                                "FROM IgnoredTag it " +
                                "JOIN it.ignoredTag t " +
                                "WHERE it.user.id = :id")
                .setParameter("id", id)
                .unwrap(org.hibernate.query.Query.class)
                .getResultList();
    }

    @Override
    public List<TrackedTagDto> getTrackedByUserId(Long id) {
        return entityManager.createQuery(
                "select new com.javamentor.qa.platform.models.dto.TrackedTagDto(" +
                        "t.id, " +
                        "t.name) " +
                        "from TrackedTag tt join tt.trackedTag t where tt.user.id = :id", TrackedTagDto.class)
                .setParameter("id", id)
                .getResultList();
    }

    private static class QuestionIdTagDtoMapResultTransformer implements ResultTransformer {

        private final Map<Long, List<TagDto>> result = new HashMap<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            long questionId = (Long) tuple[0];
            long tagId = (Long) tuple[1];
            String tagName = (String) tuple[2];
            String tagDescription = (String) tuple[3];

            TagDto tagDto = new TagDto(tagId, tagName, tagDescription);

            if (!result.containsKey(questionId)) {
                result.put(questionId, new ArrayList<>());
            }
            result.get(questionId).add(tagDto);
            return tuple;
        }

        @Override
        public List transformList(List list) {
            return Collections.singletonList(result);
        }
    }
    @Override
    public List<TagDto> getTop3TagsByUserId(Long id) {
        return entityManager.createNativeQuery(
                "SELECT q_h_t.tag_id AS id, COUNT(q_h_t.tag_id) AS reputation, \n" +
                        "                        t.name AS name, \n" +
                        "                        t.description as description \n" +
                        "                        FROM reputation r \n" +
                        "                        JOIN question_has_tag q_h_t \n" +
                        "                        ON r.question_id = q_h_t.question_id \n" +
                        "                        JOIN tag t \n" +
                        "                        ON q_h_t.tag_id = t.id \n" +
                        "                        WHERE r.type = 3 AND r.author_id = :author_id \n" +
                        "                        GROUP BY q_h_t.tag_id, t.name, t.description \n" +
                        "                        ORDER BY COUNT(q_h_t.tag_id) DESC \n" +
                        "                        LIMIT 3")
                .unwrap(SQLQuery.class)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("reputation")
                .addScalar("name")
                .addScalar("description")
                .setParameter("author_id", id)
                .setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] tuples, String[] aliases) {
                        TagDto tagDto = new TagDto();
                        tagDto.setId((long) tuples[0]);
                        tagDto.setName((String) tuples[2]);
                        tagDto.setDescription((String) tuples[3]);
                        return tagDto;
                    }

                    @Override
                    public List transformList(List list) {
                        return list;
                    }
                })
                .getResultList();
    }
}
