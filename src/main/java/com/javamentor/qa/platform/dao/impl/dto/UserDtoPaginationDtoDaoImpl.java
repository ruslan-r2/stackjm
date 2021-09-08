package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.PaginationDao;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@Repository("allUsers")
public class UserDtoPaginationDtoDaoImpl implements PaginationDao<UserDto> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int getCountOfAllItems(Map<String, Object> parameters) {
        Long count = entityManager
                .createQuery("SELECT count(user) FROM User user WHERE user.isDeleted = false", Long.class)
                .getSingleResult();
        return count.intValue();
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<UserDto> getItems(Map<String, Object> parameters) {
        int itemsOnPage = (int) parameters.get("itemsOnPage");
        int currentPage = (int) parameters.get("currentPage");

        return entityManager.createQuery("SELECT " +
                "user.id as id, " +
                "user.email as email, " +
                "user.fullName as fullName, " +
                "user.imageLink as linkImage, " +
                "user.city as city, " +
                "CAST((SELECT coalesce(SUM(r.count),0) FROM Reputation r WHERE r.sender.id = user.id) as int) as reputation FROM User user WHERE user.isDeleted = false ORDER BY user.id")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(UserDto.class))
                .setFirstResult(itemsOnPage * (currentPage - 1))
                .setMaxResults(itemsOnPage)
                .getResultList();
    }
}
