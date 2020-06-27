package org.xmc.be.services.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserRepository;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.common.stubs.login.DtoBootstrapFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginService.class);

    public static final String SYSTEM_PROPERTY_DISPLAYNAME = "user.displayName";

    private final UserRepository userRepository;
    private final BootstrapFileController bootstrapFileController;

    @Autowired
    public UserLoginService(
            UserRepository userRepository,
            BootstrapFileController bootstrapFileController) {

        this.userRepository = userRepository;
        this.bootstrapFileController = bootstrapFileController;
    }

    public void login(DtoBootstrapFile dtoBootstrapFile) {
        String username = dtoBootstrapFile.getUsername();
        LOGGER.info("Logging in user '{}'.", username);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setLastLogin(LocalDateTime.now());
            userRepository.save(user.get());
        } else {
            throw new RuntimeException(String.format("Could not find user for username '%s'.", username));
        }

        System.clearProperty("user.name");
        System.clearProperty("user.password");
        System.clearProperty("user.database.dir");
        System.setProperty(SYSTEM_PROPERTY_DISPLAYNAME, user.get().getDisplayName());

        bootstrapFileController.writeBootstrapFile(dtoBootstrapFile);
    }
}
