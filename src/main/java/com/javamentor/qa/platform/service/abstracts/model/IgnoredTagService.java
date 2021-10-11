package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;

public interface IgnoredTagService extends ReadWriteService<IgnoredTag, Long> {
    Tag add(Long tagId, User user);
}
