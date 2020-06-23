package org.xmc.be.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
