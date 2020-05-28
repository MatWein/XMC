package org.xmc.be.services.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserRepository;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.be.services.login.dto.DtoBootstrapFile;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(DtoBootstrapFile dtoBootstrapFile) {
        LOGGER.info("Logging in user '{}'.", dtoBootstrapFile.getUsername());

        User user = userRepository.findByUsername(dtoBootstrapFile.getUsername());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        System.clearProperty("user.name");
        System.clearProperty("user.password");
        System.clearProperty("user.database.dir");

        BootstrapFileController.writeBootstrapFile(dtoBootstrapFile);
    }
}
