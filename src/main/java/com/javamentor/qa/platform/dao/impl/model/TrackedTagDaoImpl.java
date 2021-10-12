package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TrackedTagDaoImpl extends ReadWriteDaoImpl<TrackedTag, Long> implements TrackedTagDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<TrackedTag> getByUserAndTag(User user, Tag tag) {
        TypedQuery<TrackedTag> query = entityManager
                .createQuery("SELECT tt FROM TrackedTag tt WHERE tt.user.id=:userId AND tt.trackedTag.id=:tagId",
                        TrackedTag.class)
                .setParameter("userId", user.getId())
                .setParameter("tagId", tag.getId());
        return SingleResultUtil.getSingleResultOrNull(query);
    }

}
