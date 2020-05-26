package org.xmc.be.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
