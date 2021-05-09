package io.github.matwein.xmc.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.BinaryData;

public interface BinaryDataJpaRepository extends JpaRepository<BinaryData, Long> {
}
