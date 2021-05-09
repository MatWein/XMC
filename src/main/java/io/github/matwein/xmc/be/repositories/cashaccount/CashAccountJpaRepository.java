package io.github.matwein.xmc.be.repositories.cashaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;

import java.util.List;

public interface CashAccountJpaRepository extends JpaRepository<CashAccount, Long> {
	List<CashAccount> findByDeletionDateIsNull();
}
