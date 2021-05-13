package io.github.matwein.xmc.be.services.login;

import io.github.matwein.xmc.be.entities.user.User;
import io.github.matwein.xmc.be.repositories.user.UserJpaRepository;
import io.github.matwein.xmc.common.services.login.IUserLoginService;
import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserLoginService implements IUserLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginService.class);

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public UserLoginService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public String login(DtoBootstrapFile dtoBootstrapFile) {
        String username = dtoBootstrapFile.getUsername();
        LOGGER.info("Logging in user '{}'.", username);

        Optional<User> user = userJpaRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setLastLogin(LocalDateTime.now());
            userJpaRepository.save(user.get());
            return user.get().getDisplayName();
        } else {
            throw new RuntimeException(String.format("Could not find user for username '%s'.", username));
        }
    }
}
