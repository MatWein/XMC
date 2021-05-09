package io.github.matwein.xmc.be.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.user.ServiceCallLog;

public interface ServiceCallLogJpaRepository extends JpaRepository<ServiceCallLog, Long> {
}
