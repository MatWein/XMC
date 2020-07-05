package org.xmc.be.repositories.cashaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.cashaccount.CashAccount;

public interface CashAccountJpaRepository extends JpaRepository<CashAccount, Long> {
}
