package com.epam.esm.repository;

import com.epam.esm.entity.UserRole;
import com.epam.esm.entity.UserRoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface User Role repository.
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    /**
     * Find User Role by name of User Role.
     *
     * @param roleName the User Role name
     * @return the User Role
     */
    UserRole findUserRoleByName(UserRoleName roleName);
}
