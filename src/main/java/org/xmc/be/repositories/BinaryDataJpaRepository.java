package org.xmc.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.BinaryData;

public interface BinaryDataJpaRepository extends JpaRepository<BinaryData, Long> {
}
