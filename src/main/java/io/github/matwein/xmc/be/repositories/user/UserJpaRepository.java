package io.github.matwein.xmc.be.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.user.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
