package org.xmc.be.repositories.cashaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashAccountTransactionJpaRepository extends JpaRepository<CashAccountTransaction, Long> {
    List<CashAccountTransaction> findByValutaDateGreaterThanEqualOrderByValutaDate(LocalDate startDate);

    Optional<CashAccountTransaction> findFirstByValutaDateIsBeforeOrderByValutaDateDesc(LocalDate valutaDate);
}
