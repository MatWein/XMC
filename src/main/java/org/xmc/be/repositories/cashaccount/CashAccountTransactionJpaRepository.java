package org.xmc.be.repositories.cashaccount;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CashAccountTransactionJpaRepository extends JpaRepository<CashAccountTransaction, Long> {
    @Query("SELECT cat FROM CashAccountTransaction cat " +
            "WHERE cat.valutaDate >= :startDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount " +
            "ORDER BY cat.valutaDate, cat.creationDate, cat.id"
    )
    List<CashAccountTransaction> findTransactionsAfterDate(CashAccount cashAccount, LocalDate startDate);

    @Query("SELECT cat FROM CashAccountTransaction cat " +
            "WHERE cat.valutaDate < :valutaDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount " +
            "ORDER BY cat.valutaDate DESC, cat.creationDate DESC, cat.id DESC"
    )
    List<CashAccountTransaction> findTransactionsBeforeDate(CashAccount cashAccount, LocalDate valutaDate, Pageable pageable);

    default Optional<CashAccountTransaction> findFirstTransactionBeforeDate(CashAccount cashAccount, LocalDate valutaDate) {
        return findTransactionsBeforeDate(cashAccount, valutaDate, PageRequest.of(0, 1)).stream().findFirst();
    }
}
