package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Repository
public class TagDtoDaoImpl implements TagDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

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
//    String hql = "INSERT INTO Employee(firstName, lastName, salary)"  +
//            "SELECT firstName, lastName, salary FROM old_employee";
    @Override
    public TagDto addTagToIgnoreTag(Long id, String userMail) {
        TagDto tagDto = entityManager.createQuery(
                "INSERT INTO IgnoredTag(Long id, Tag ignoredTag, User user)" +
                        "SELECT " + id + ", Tag ignoredTag, User user" +
                        "WHERE "
        )
        return null;
    }

}
