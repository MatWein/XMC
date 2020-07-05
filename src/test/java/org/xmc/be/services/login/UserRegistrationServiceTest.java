package org.xmc.be.services.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.xmc.JUnitTestBase;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserJpaRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserRegistrationServiceTest extends JUnitTestBase {
    private UserRegistrationService service;

    @Mock
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        service = new UserRegistrationService(userJpaRepository);
    }

    @Test
    void testRegisterNewUser() {
        String username = "username";
        String displayName = "displayName";

        when(userJpaRepository.save(any(User.class))).thenReturn(null);

        service.registerNewUser(username, displayName);
    }
}