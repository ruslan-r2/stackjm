package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class QuestionDtoDaoImpl implements QuestionDtoDao{
    @PersistenceContext
    private EntityManager entityManager;
    private final TagDtoDao tagDtoDao;

    public QuestionDtoDaoImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public Optional<QuestionDto> getById(Long id) {
            Session session = entityManager.unwrap(Session.class);
            Query query = session.createQuery("select q.id as id, " +
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
                    .setResultTransformer(new AliasToBeanResultTransformer(QuestionDto.class)).unwrap(TypedQuery.class);
            Optional<QuestionDto> optionalQuestionDto = Optional.of((QuestionDto) query.getSingleResult());
            optionalQuestionDto.get().setListTagDto(tagDtoDao.getByQuestionId(id));
            return optionalQuestionDto;
    }
}
