package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class IgnoredTagDaoImpl extends ReadWriteDaoImpl<IgnoredTag, Long> implements IgnoredTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<IgnoredTag> getByUserAndTag(User user, Tag tag) {
        TypedQuery<IgnoredTag> query = entityManager
                .createQuery("SELECT it FROM IgnoredTag it WHERE it.user.id=:userId AND it.ignoredTag.id=:tagId",
                        IgnoredTag.class)
                .setParameter("userId", user.getId())
                .setParameter("tagId", tag.getId());
        return SingleResultUtil.getSingleResultOrNull(query);
    }

}
