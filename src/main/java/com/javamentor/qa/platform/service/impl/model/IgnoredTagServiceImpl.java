package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.exception.TagAlreadyExistsException;
import com.javamentor.qa.platform.exception.TagNotFoundException;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {

    private final IgnoredTagDao ignoredTagDao;
    private final TagService tagService;

    @Autowired
    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao, TagService tagService) {
        super(ignoredTagDao);
        this.ignoredTagDao = ignoredTagDao;
        this.tagService = tagService;
    }

    @Transactional
    public Tag add(Long tagId, User user) {
        Optional<Tag> tag = tagService.getById(tagId);
        if (!tag.isPresent()) {
            throw new TagNotFoundException("Такого тега не существует");
        }
        if (ignoredTagDao.getByUserAndTag(user, tag.get()).isPresent()) {
            throw new TagAlreadyExistsException("Тег уже был добавлен в игнорируемые ранее");
        }
        super.persist(new IgnoredTag(tag.get(), user));
        return tag.get();
    }

}
