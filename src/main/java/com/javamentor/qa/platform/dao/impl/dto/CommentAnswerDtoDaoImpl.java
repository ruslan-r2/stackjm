package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentAnswerDtoDao;
import com.javamentor.qa.platform.models.dto.CommentAnswerDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentAnswerDtoDaoImpl implements CommentAnswerDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CommentAnswerDto getCommentAnswerDtoById(Long answerId) {
        return (CommentAnswerDto) entityManager.createQuery("SELECT " +
                "commentans.id as id, " +
                "commentans.answer.id as answerId, " +
                "commentans.comment.lastUpdateDateTime as lastRedactionDate ," +
                "commentans.comment.persistDateTime as persistDate, " +
                "commentans.comment.text as text," +
                "commentans.comment.user.id as userId," +
                "commentans.comment.user.imageLink as imageLink," +
                "(SELECT coalesce(SUM(rep.count), 0L) FROM Reputation rep " +
                "WHERE rep.author.id = commentans.comment.user.id ) as reputation " +
                "FROM CommentAnswer commentans WHERE answer.id = :id")
                .setParameter("id", answerId)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(CommentAnswerDto.class))
                .getSingleResult();
    }
}
