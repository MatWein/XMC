package io.github.matwein.xmc.be.services.login;

import io.github.matwein.xmc.be.entities.user.User;
import io.github.matwein.xmc.be.repositories.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserRegistrationServiceTest {
    private UserRegistrationService service;

    @Mock
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
	    MockitoAnnotations.openMocks(this);
	    
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