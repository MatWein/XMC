package org.xmc.be.repositories.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.Bank;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
}
