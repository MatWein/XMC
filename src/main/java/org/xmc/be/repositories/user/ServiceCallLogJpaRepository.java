package org.xmc.be.repositories.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.user.ServiceCallLog;

public interface ServiceCallLogJpaRepository extends JpaRepository<ServiceCallLog, Long> {
}
