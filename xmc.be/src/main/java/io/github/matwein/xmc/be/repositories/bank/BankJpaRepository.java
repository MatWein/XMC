package io.github.matwein.xmc.be.repositories.bank;

import io.github.matwein.xmc.be.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
    List<Bank> findByDeletionDateIsNull();
}
