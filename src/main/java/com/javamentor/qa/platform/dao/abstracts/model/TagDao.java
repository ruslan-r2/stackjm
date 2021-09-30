package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.Collection;
import java.util.List;

public interface TagDao extends ReadWriteDao<Tag, Long> {

    List<Tag> getTagsByNames(Collection<String> names);
}
