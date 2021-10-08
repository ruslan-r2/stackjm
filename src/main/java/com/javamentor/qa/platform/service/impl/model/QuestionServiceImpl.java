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
        List<Tag> notExistingTags = new ArrayList<>();

        tags.forEach(tag -> {
            if ( !existingTags.isEmpty() ) {
                for (Tag t : existingTags) {
                    if (t.getName().equals(tag.getName())) {
                        List<Question> listQuestion = t.getQuestions();
                        if (listQuestion == null) {
                            listQuestion = new ArrayList<>();
                        }
                        listQuestion.add(question);
                        t.setQuestions(listQuestion);
                        notExistingTags.add(t);
                    }
                }
            }else{
                List<Question> listQuestion = tag.getQuestions();
                if (listQuestion == null) {
                    listQuestion = new ArrayList<>();
                }
                listQuestion.add(question);
                tag.setQuestions(listQuestion);
                notExistingTags.add(tag);
            }

        });
        question.setTags(notExistingTags);
        questionDao.persist(question);
    }
}