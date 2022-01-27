package com.epam.esm.repository;

import com.epam.esm.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.entity.UserRoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Test
    void testFindByName() {
        //Given
        UserRole expected = provideRole();

        //When
        UserRole actual = userRoleRepository.findUserRoleByName(USER);

        //Then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    private UserRole provideRole() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setName(USER);
        return role;
    }
}
