package io.github.matwein.xmc.be.repositories.cashaccount;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionOverviewFields;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface CashAccountTransactionRepository extends JpaRepository<CashAccountTransaction, Long> {
    default QueryResults<DtoCashAccountTransactionOverview> loadOverview(
    		CashAccount cashAccountEntity,
		    PagingParams<CashAccountTransactionOverviewFields> pagingParams) {
	    
	    Pageable pageable = toPageable(pagingParams, CashAccountTransactionOverviewFields.VALUTA_DATE, Order.DESC);
	    
	    Sort extendedSort = pageable.getSort()
			    .and(Sort.by("cat.creationDate").descending())
			    .and(Sort.by("cat.id").descending());
	    
	    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), extendedSort);
		
	    return fromPage(loadOverview$(
			    pageable,
			    StringUtils.defaultString(pagingParams.getFilter()),
			    cashAccountEntity.getId()));
    }
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransactionOverview(
			cat.id, c.id, c.name, i.rawData, ca.currency, cat.saldoBefore, cat.saldoAfter,
			cat.usage, cat.description, cat.valutaDate, cat.value, cat.reference, cat.referenceIban,
			cat.referenceBank, cat.creditorIdentifier, cat.mandate, cat.creationDate
		)
		from CashAccountTransaction cat
		inner join cat.cashAccount ca
		left join cat.category c
		left join c.icon i
		where cat.deletionDate is null and ca.id = :cashAccountId and (
			cat.usage ilike '%' || :filter || '%'
			or cat.description ilike '%' || :filter || '%'
			or c.name ilike '%' || :filter || '%'
		)
	""")
	Page<DtoCashAccountTransactionOverview> loadOverview$(
			Pageable pageable,
			@Param("filter") String filter,
			@Param("cashAccountId") Long cashAccountId);
}
