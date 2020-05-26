package org.xmc.be.services.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.user.User;
import org.xmc.be.repositories.user.UserRepository;

@Service
@Transactional
public class UserRegistrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUser(String username, String displayName) {
        LOGGER.info("Registering new user with username '{}' and display name '{}'.", username, displayName);

        User user = new User();
        user.setUsername(username);
        user.setDisplayName(displayName);
        userRepository.save(user);
    }
}
