package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Optional;

public interface TrackedTagDao extends ReadWriteDao<TrackedTag, Long>{
    Optional<TrackedTag> getByUserAndTag(User user, Tag tag);
}
