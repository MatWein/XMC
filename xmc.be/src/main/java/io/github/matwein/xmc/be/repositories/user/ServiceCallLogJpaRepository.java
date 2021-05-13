package io.github.matwein.xmc.be.repositories.user;

import io.github.matwein.xmc.be.entities.user.ServiceCallLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCallLogJpaRepository extends JpaRepository<ServiceCallLog, Long> {
}
