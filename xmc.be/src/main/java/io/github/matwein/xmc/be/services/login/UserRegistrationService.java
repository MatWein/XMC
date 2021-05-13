package io.github.matwein.xmc.be.services.login;

import io.github.matwein.xmc.be.entities.user.User;
import io.github.matwein.xmc.be.repositories.user.UserJpaRepository;
import io.github.matwein.xmc.common.services.login.IUserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRegistrationService implements IUserRegistrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationService.class);

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserRegistrationService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void registerNewUser(String username, String displayName) {
        LOGGER.info("Registering new user with username '{}' and display name '{}'.", username, displayName);

        User user = new User();
        user.setUsername(username);
        user.setDisplayName(displayName);
        userJpaRepository.save(user);
    }
}
