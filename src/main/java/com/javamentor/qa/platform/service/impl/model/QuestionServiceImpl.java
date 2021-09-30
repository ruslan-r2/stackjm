package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.dao.impl.model.QuestionDaoImpl;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private QuestionDao questionDao;
    private TagDao tagDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        super(questionDao);
        this.questionDao = questionDao;
    }

    @Override
    public long getCountQuestion() {
        return questionDao.getCountQuestion();

    }

    @Override
    public void persist(Question question) {
        List<Tag> tags = question.getTags();
        List<String> tagNames = new ArrayList<>();
        tags.forEach(tag -> {
            tagNames.add(tag.getName());
        });
        List<Tag> existingTags = tagDao.getTagsByNames(tagNames);
        List<Tag> notExistingTags = new ArrayList<>();
        tags.forEach(tag -> {
            if (!existingTags.contains(tag)) {
                notExistingTags.add(tag);
            }
        });
        tagDao.persistAll(notExistingTags);
        question.setTags(notExistingTags);
        questionDao.persist(question);
    }
}
