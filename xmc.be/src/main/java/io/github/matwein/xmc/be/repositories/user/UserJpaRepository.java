package io.github.matwein.xmc.be.repositories.user;

import io.github.matwein.xmc.be.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
