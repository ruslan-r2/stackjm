package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.AnswerDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class AnswerDaoImpl extends ReadWriteDaoImpl<Answer, Long> implements AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void deleteById(Long id) {
        entityManager.createQuery("update Answer as a set a.isDeleted = true where a.id = :id").setParameter("id", id).executeUpdate();
    }

    @Override
    public Optional<Answer> getAnswerForVote(Long answerId, Long userId) {
        Query query = entityManager.createQuery("select a from Answer a where a.id = :ansId and not a.user.id = :userId")
                .setParameter("ansId", answerId)
                .setParameter("userId",userId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

    //пробовал использовать обычный update, для него не работает. Поэтому создал update, который обновляет не все поля, ап только указанные в задании
    @Override
    public void updateAnswerSpecial(Long answerId, Answer answer) {

        entityManager.createQuery("UPDATE Answer a SET a.htmlBody = :body, a.updateDateTime = :time WHERE a.id = :id ")
                .setParameter("id", answerId)
                .setParameter("body", answer.getHtmlBody())
                .setParameter("time", LocalDateTime.now())
                .executeUpdate();
    }

    //    Необходимость переопределения метода getById связана с проверкой isDeleted = false, которой нет в дефолтной реализации getById
    @Override
    public Optional<Answer> getById(Long answerId) {
        Query query = entityManager.createQuery("SELECT a FROM Answer a where a.id = :id and a.isDeleted = false")
                .setParameter("id", answerId);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
}
