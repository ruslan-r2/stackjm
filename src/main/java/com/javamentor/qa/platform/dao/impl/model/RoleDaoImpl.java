package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.entity.user.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class RoleDaoImpl extends ReadWriteDaoImpl<Role, Long> implements RoleDao {

    public Optional<Role> getByName(String name) {
        String hql = "FROM Role WHERE name = :name";
        TypedQuery<Role> query = (TypedQuery<Role>) entityManager.createQuery(hql).setParameter("name", name);
        return SingleResultUtil.getSingleResultOrNull(query);
    }

}