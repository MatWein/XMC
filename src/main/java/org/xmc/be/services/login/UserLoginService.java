package org.xmc.be.services.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserRepository;

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

    public void login(String username) {
        LOGGER.info("Logging in user '{}'.", username);

        User user = userRepository.findByUsername(username);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }
}
