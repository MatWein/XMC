package io.github.matwein.xmc.be.repositories.cashaccount;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CashAccountTransactionJpaRepository extends JpaRepository<CashAccountTransaction, Long> {
    @Query("SELECT cat FROM CashAccountTransaction cat " +
            "WHERE cat.valutaDate >= :startDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount " +
            "ORDER BY cat.valutaDate, cat.creationDate, cat.id"
    )
    List<CashAccountTransaction> findTransactionsAfterDate(CashAccount cashAccount, LocalDate startDate);
	
	@Query("SELECT cat FROM CashAccountTransaction cat " +
			"WHERE cat.valutaDate >= :startDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount AND cat.category is not null " +
			"ORDER BY cat.valutaDate, cat.creationDate, cat.id"
	)
	List<CashAccountTransaction> findTransactionsOnOrAfterDateHavingCategory(CashAccount cashAccount, LocalDate startDate);

    @Query("SELECT cat FROM CashAccountTransaction cat " +
            "WHERE cat.valutaDate < :valutaDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount " +
            "ORDER BY cat.valutaDate DESC, cat.creationDate DESC, cat.id DESC"
    )
    List<CashAccountTransaction> findTransactionsBeforeDate(CashAccount cashAccount, LocalDate valutaDate, Pageable pageable);
	
	default Optional<CashAccountTransaction> findFirstTransactionBeforeDate(CashAccount cashAccount, LocalDate valutaDate) {
		return findTransactionsBeforeDate(cashAccount, valutaDate, PageRequest.of(0, 1)).stream().findFirst();
	}
	
    @Query("SELECT cat FROM CashAccountTransaction cat " +
            "WHERE cat.valutaDate <= :valutaDate AND cat.deletionDate IS NULL AND cat.cashAccount = :cashAccount AND cat.creationDate <= :creationDate AND cat.id < :maxId " +
            "ORDER BY cat.valutaDate DESC, cat.creationDate DESC, cat.id DESC"
    )
    List<CashAccountTransaction> findTransactionsBeforeOrOnDate(
    		CashAccount cashAccount,
		    LocalDate valutaDate,
		    LocalDateTime creationDate,
		    long maxId,
		    Pageable pageable);

    default Optional<CashAccountTransaction> findFirstTransactionBeforeOrOnDate(
    		CashAccount cashAccount,
		    LocalDate valutaDate,
		    LocalDateTime creationDate,
		    long maxId) {
    	
        return findTransactionsBeforeOrOnDate(cashAccount, valutaDate, creationDate, maxId, PageRequest.of(0, 1)).stream().findFirst();
    }
    
    List<CashAccountTransaction> findByCashAccountAndDeletionDateIsNull(CashAccount cashAccount);
	
	@Query("SELECT cat FROM CashAccountTransaction cat " +
			"WHERE cat.deletionDate IS NULL AND cat.cashAccount.id in (:cashAccountIds) " +
			"ORDER BY cat.valutaDate DESC, cat.creationDate DESC, cat.id DESC"
	)
    List<CashAccountTransaction> findMostRecentTransactions(Collection<Long> cashAccountIds, Pageable pageable);
	
	@Query("SELECT cat FROM CashAccountTransaction cat " +
			"WHERE cat.deletionDate IS NULL AND cat.cashAccount.id in (:cashAccountIds) " +
			"ORDER BY cat.valutaDate ASC, cat.creationDate ASC, cat.id ASC"
	)
    List<CashAccountTransaction> findAllTransactions(Collection<Long> cashAccountIds, Pageable pageable);
	
	default Optional<CashAccountTransaction> findFirstTransaction(Collection<Long> cashAccountIds) {
		return findAllTransactions(cashAccountIds, PageRequest.of(0, 1)).stream().findFirst();
	}
}
