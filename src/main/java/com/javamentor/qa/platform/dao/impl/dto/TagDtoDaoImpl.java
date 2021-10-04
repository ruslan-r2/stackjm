package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.IgnoredTagDto;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.dto.TrackedTagDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

}
