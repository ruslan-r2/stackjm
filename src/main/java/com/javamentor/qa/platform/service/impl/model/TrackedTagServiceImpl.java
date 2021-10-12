package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.exception.TagAlreadyExistsException;
import com.javamentor.qa.platform.exception.TagNotFoundException;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {

    private final TrackedTagDao trackedTagDao;
    private final TagService tagService;

    @Autowired
    public TrackedTagServiceImpl(TrackedTagDao trackedTagDao, TagService tagService) {
        super(trackedTagDao);
        this.trackedTagDao = trackedTagDao;
        this.tagService = tagService;
    }

    @Transactional
    public Tag add(Long tagId, User user) {
        Optional<Tag> tag = tagService.getById(tagId);
        if (!tag.isPresent()) {
            throw new TagNotFoundException("тег с таким id не найден");
        }
        if (trackedTagDao.getByUserAndTag(user, tag.get()).isPresent()) {
            throw new TagAlreadyExistsException("тег уже был добавлен в отслеживаемые ранее");
        }
        super.persist(new TrackedTag(tag.get(), user));
        return tag.get();
    }

}
