package org.xmc.be.repositories.user;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.user.User;

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

        Assert.assertEquals(user, result.get());
    }

    @Test
    void testFindByUsername_NotFound() {
        graphGenerator.createUser();
        graphGenerator.createUser();

        flushAndClear();

        Optional<User> result = userJpaRepository.findByUsername("test");

        Assert.assertEquals(Optional.empty(), result);
    }
}