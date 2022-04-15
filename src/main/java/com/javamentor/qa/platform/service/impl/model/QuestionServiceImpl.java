package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Question;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl extends ReadWriteServiceImpl<Question, Long> implements QuestionService {

    private QuestionDao questionDao;
    private TagDao tagDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao, TagDao tagDao) {
        super(questionDao);
        this.questionDao = questionDao;
        this.tagDao = tagDao;
    }

    @Override
    public long getCountQuestion() {
        return questionDao.getCountQuestion();

    }

    @Override
    @Transactional
    public void persist(Question question) {
        List<Tag> tags = question.getTags();
        List<String> tagNames = new ArrayList<>();
        tags.forEach(tag -> tagNames.add(tag.getName()));
        List<Tag> existingTags = tagDao.getTagsByNames(tagNames);
        List<Tag> newTags = new ArrayList<>();

        tags.forEach(tag -> {
            for (Tag t : existingTags ) {
                if (tag.getName().equals(t.getName())) {
                    newTags.add(t);
                    tag.setId(t.getId());
                }
            }
        });
        tags.forEach(tag -> {
            if (tag.getId() == null ) {
                newTags.add(tag);
            }
        });

        question.setTags(newTags);
        questionDao.persist(question);
    }
}