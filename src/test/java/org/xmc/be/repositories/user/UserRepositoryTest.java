package org.xmc.be.repositories.user;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.user.User;

import java.util.Optional;

class UserRepositoryTest extends IntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        graphGenerator.createUser();
        String username = "user1";
        User user = graphGenerator.createUser(username);
        graphGenerator.createUser();

        flushAndClear();

        Optional<User> result = userRepository.findByUsername(username);

        Assert.assertEquals(user, result.get());
    }

    @Test
    void testFindByUsername_NotFound() {
        graphGenerator.createUser();
        graphGenerator.createUser();

        flushAndClear();

        Optional<User> result = userRepository.findByUsername("test");

        Assert.assertEquals(Optional.empty(), result);
    }
}