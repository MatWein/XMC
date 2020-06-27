package org.xmc.be.services.login;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.xmc.JUnitTestBase;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserRepository;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.common.stubs.login.DtoBootstrapFile;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserLoginServiceTest extends JUnitTestBase {
    private UserLoginService service;

    @Mock private UserRepository userRepository;
    @Mock private BootstrapFileController bootstrapFileController;

    @BeforeEach
    void setUp() {
        service = new UserLoginService(userRepository, bootstrapFileController);
    }

    @AfterEach
    void tearDown() {
        System.clearProperty(UserLoginService.SYSTEM_PROPERTY_DISPLAYNAME);
    }

    @Test
    void testLogin() {
        String username = "test";

        DtoBootstrapFile dtoBootstrapFile = new DtoBootstrapFile();
        dtoBootstrapFile.setUsername(username);

        User userInstance = testObjectFactory.create(User.class);
        userInstance.setDisplayName("displayName");

        Optional<User> user = Optional.of(userInstance);
        when(userRepository.findByUsername(username)).thenReturn(user);

        when(userRepository.save(user.get())).thenReturn(null);

        service.login(dtoBootstrapFile);

        verify(bootstrapFileController).writeBootstrapFile(dtoBootstrapFile);
    }

    @Test
    void testLogin_UserNotFound() {
        String username = "test";

        DtoBootstrapFile dtoBootstrapFile = new DtoBootstrapFile();
        dtoBootstrapFile.setUsername(username);

        Optional<User> user = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(user);

        Assert.assertThrows(RuntimeException.class, () -> service.login(dtoBootstrapFile));
    }
}