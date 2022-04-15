package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.models.entity.user.User;

import java.util.Collection;

public interface ReadWriteDao<E, K> extends ReadOnlyDao<E, K> {
    void persist(E e);

    void update(E e);

    void delete(E e);

    void persistAll(E... entities);

    void persistAll(Collection<E> entities);

    void deleteAll(Collection<E> entities);

    void updateAll(Iterable<? extends E> entities);

    void deleteById(K id);

    void resetPassword(User user);

    void updateUserPublicInfo(User user);
}
