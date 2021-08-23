package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User, Long> implements UserDao {


    public User getUserByToken(String token) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.token = :token", User.class)
                .setParameter("token", token)
                .getSingleResult();
//        String hql = "FROM User WHERE token = :token";
//        TypedQuery<User> query = (TypedQuery<User>) entityManager.createQuery(hql).setParameter("token", token);
//        return SingleResultUtil.getSingleResultOrNull(query);
    }
}