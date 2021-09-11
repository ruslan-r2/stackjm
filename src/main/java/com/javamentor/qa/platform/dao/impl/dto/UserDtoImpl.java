package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserDtoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDto> getById(Long id) {
        TypedQuery<UserDto> typedQuery = entityManager.createQuery(
                "SELECT NEW com.javamentor.qa.platform.models.dto.UserDto(" +
                        "u.id," +
                        "u.email," +
                        "u.fullName," +
                        "u.imageLink," +
                        "u.city," +
                        "CAST(COALESCE(SUM(r.count), 0) AS java.lang.Integer)) " +
                        "FROM Reputation r RIGHT JOIN r.author u WHERE u.id = :id GROUP BY u.id, u.email, u.fullName, u.imageLink, u.city",
                UserDto.class).setParameter("id", id);
        return SingleResultUtil.getSingleResultOrNull(typedQuery);
    }
}
