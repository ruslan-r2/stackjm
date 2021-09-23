package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionCommentDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentDtoDaoImpl implements CommentDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("deprecation")
    public List<QuestionCommentDto> getAllQuestionCommentDtoById(Long questionId) {
        return entityManager.createQuery("SELECT " +
                "comment.id as id, " +
                "comment.question.id as questionId, " +
                "comment.comment.lastUpdateDateTime as lastRedactionDate ," +
                "comment.comment.persistDateTime as persistDate, " +
                "comment.comment.text as text," +
                "comment.comment.user.id as userId," +
                "comment.comment.user.imageLink as imageLink," +
                "(SELECT coalesce(SUM(rep.count), 0L) FROM Reputation rep " +
                "WHERE rep.author.id = comment.comment.user.id ) as reputation " +
                "FROM CommentQuestion comment WHERE question.id = :id")
                .setParameter("id", questionId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(QuestionCommentDto.class))
                .getResultList();
    }
}
