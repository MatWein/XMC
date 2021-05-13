package io.github.matwein.xmc.be.repositories;

import io.github.matwein.xmc.be.entities.BinaryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryDataJpaRepository extends JpaRepository<BinaryData, Long> {
}
