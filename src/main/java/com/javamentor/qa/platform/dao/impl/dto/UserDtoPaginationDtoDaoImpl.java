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

        String sorted = "ORDER BY";

        if (parameters.containsKey("sorted-reputation") && parameters.get("sorted-reputation").equals(true)){
            sorted += " reputation DESC, ";
        }
        if (parameters.containsKey("sorted-votes") && parameters.get("sorted-votes").equals(true)){
            sorted += " votes DESC, ";
        }
        if (parameters.containsKey("sorted-registrationDate") && parameters.get("sorted-registrationDate").equals(true)){
            sorted += " registrationDate DESC, ";
        }

        sorted += " user.id";

        return entityManager.createQuery("SELECT " +
                "user.id as id, " +
                "user.email as email, " +
                "user.fullName as fullName, " +
                "user.imageLink as linkImage, " +
                "user.city as city, " +
                "(SELECT coalesce(SUM(r.count),0) FROM Reputation r WHERE r.author.id = user.id) as reputation, " +
                "user.persistDateTime as registrationDate," +
                "((SELECT COUNT(*) FROM VoteQuestion vq WHERE vq.user.id = user.id) + (SELECT COUNT(*) FROM VoteAnswer va WHERE va.user.id = user.id)) as votes " +
                "FROM User user WHERE user.isDeleted = false " + sorted)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.aliasToBean(UserDto.class))
                .setFirstResult(itemsOnPage * (currentPage - 1))
                .setMaxResults(itemsOnPage)
                .getResultList();
    }
}
