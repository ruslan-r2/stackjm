package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;


public abstract class ReadWriteDaoImpl<E, K> extends ReadOnlyDaoImpl<E, K> {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(E e) {
        entityManager.persist(e);
    }
    @Transactional
    public void update(E e) {
        entityManager.merge(e);
    }

    public void delete(E e) {
        entityManager.remove(e);
    }


    public void deleteById(K id) {
        Class<E> clazz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        String hql = "DELETE " + clazz.getName() + " WHERE id = :id";
        entityManager.createQuery(hql).setParameter("id", id).executeUpdate();
    }

    public void persistAll(E... entities) {
        int i = 0;

        for (E entity : entities) {
            entityManager.persist(entity);

            i++;

            // Flush a batch of inserts and release memory
            if (i % batchSize == 0 && i > 0) {

                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }

    }

    public void persistAll(Collection<E> entities) {
        int i = 0;

        for (E entity : entities) {
            entityManager.persist(entity);

            i++;

            // Flush a batch of inserts and release memory
            if (i % batchSize == 0 && i > 0) {

                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }


    public void deleteAll(Collection<E> entities) {
        for (E entity : entities) {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        }
    }


    public void updateAll(Iterable<? extends E> entities) {
        int i = 0;

        for (E entity : entities) {
            entityManager.merge(entity);

            i++;

            // Flush a batch of inserts and release memory
            if (i % batchSize == 0 && i > 0) {

                entityManager.flush();
                entityManager.clear();
                i = 0;
            }
        }
        if (i > 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }

    public void resetPassword(User user) {
        String hql = "UPDATE User u set u.password = :password where u.id = :id";
        entityManager.createQuery(hql)
                .setParameter("password", user.getPassword())
                .setParameter("id", user.getId())
                .executeUpdate();
    }

    public void updateUserPublicInfo(User user) {
        String hql = "UPDATE User u set " +
                "u.nickname = :nickname, " +
                "u.about = :about, " +
                "u.imageLink = :imageLink, " +
                "u.linkSite = :linkSite, " +
                "u.linkVk = :linkVk, " +
                "u.linkGitHub = :linkGitHub, " +
                "u.fullName = :fullName, " +
                "u.city = :city " +
                "where u.id = :id";
        entityManager.createQuery(hql)
                .setParameter("nickname", user.getNickname())
                .setParameter("about", user.getAbout())
                .setParameter("imageLink", user.getImageLink())
                .setParameter("linkSite", user.getLinkSite())
                .setParameter("linkVk", user.getLinkVk())
                .setParameter("linkGitHub", user.getLinkGitHub())
                .setParameter("fullName", user.getFullName())
                .setParameter("city", user.getCity())
                .setParameter("id", user.getId())
                .executeUpdate();
    }
}
