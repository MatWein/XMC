package io.github.matwein.xmc.be.repositories.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.user.User;

import java.util.Optional;

class UserJpaRepositoryTest extends IntegrationTest {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void testFindByUsername() {
        graphGenerator.createUser();
        String username = "user1";
        User user = graphGenerator.createUser(username);
        graphGenerator.createUser();

        flushAndClear();

        Optional<User> result = userJpaRepository.findByUsername(username);

        Assertions.assertEquals(user, result.get());
    }

    @Test
    void testFindByUsername_NotFound() {
        graphGenerator.createUser();
        graphGenerator.createUser();

        flushAndClear();

        Optional<User> result = userJpaRepository.findByUsername("test");

        Assertions.assertEquals(Optional.empty(), result);
    }
}