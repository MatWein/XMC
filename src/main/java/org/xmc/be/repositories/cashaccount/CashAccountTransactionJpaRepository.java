package org.xmc.be.repositories.cashaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;

public interface CashAccountTransactionJpaRepository extends JpaRepository<CashAccountTransaction, Long> {
}
