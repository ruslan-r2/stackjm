package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.QuestionDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private QuestionDao questionDao;

    @Override
    public Optional<QuestionDto> getById(Long id) {
        Optional<Question> q = questionDao.getById(id);
        if(q.isPresent() && (!q.get().getIsDeleted())){
            Session session = entityManager.unwrap(Session.class);
            Query queryQ = session.createQuery("select q.id as id, " +
                    "q.title as title, " +
                    "q.user.id as authorId, " +
                    "q.user.fullName as authorName, " +
                    "q.user.imageLink as authorImage,  " +
                    "q.description as description, " +
                    "q.persistDateTime as persistDateTime, " +
                    "q.lastUpdateDateTime as lastUpdateDateTime, " +
                    "(select COALESCE(count(*), 0)  from QuestionViewed  where question.id = q.id) as viewCount," +
                    "(select COALESCE(count(*), 0)  from Answer a where question.id = q.id)   as countAnswer, " +
                    "(select COALESCE(SUM(vote), 0)  from VoteQuestion  where question.id = q.id) as countValuable " +
                    "from Question q " +
                    "where q.id = :id and q.isDeleted = false " +
                    "group by q.id, q.title, q.user.id, q.user.fullName, q.user.imageLink, q.description, q.persistDateTime, q.lastUpdateDateTime")
                    .setParameter("id", id)
                    .setResultTransformer(new AliasToBeanResultTransformer(QuestionDto.class));
            Optional<QuestionDto> optionalQuestionDto = Optional.of((QuestionDto) queryQ.getSingleResult());
            Query queryT = session.createQuery("select t.id as id, " +
                    "t.name as name, " +
                    "t.description as description " +
                    "from Question q " +
                    "join q.tags t " +
                    "where q.id = :id")
                    .setParameter("id", id).setResultTransformer(new AliasToBeanResultTransformer(TagDto.class));
            List<TagDto> list = queryT.getResultList();
            optionalQuestionDto.get().setListTagDto(list);
            return optionalQuestionDto;
        }
        return Optional.empty();
    }
}
